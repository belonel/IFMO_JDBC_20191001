package com.efimchick.ifmo.web.jdbc.service;

import com.efimchick.ifmo.web.jdbc.ConnectionSource;
import com.efimchick.ifmo.web.jdbc.domain.Department;
import com.efimchick.ifmo.web.jdbc.domain.Employee;
import com.efimchick.ifmo.web.jdbc.domain.FullName;
import com.efimchick.ifmo.web.jdbc.domain.Position;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceUtils {

    static String makePagingSQLRequest(String oldrequest, String sort, Paging paging) {
        int size = paging.itemPerPage;
        int page = paging.page;

        String request = oldrequest;
//        if (sort != null)
        request += " ORDER BY " + sort;
//        if (size != null)
        request += " LIMIT " + size;
//        if (page != null && size != null)
        request += " OFFSET " + size * page;
        //just fixing the name of the field
        request = request.replace("hired", "hiredate");
        return request;
    }

    static List<Employee> getEmployeeResultListByRequest(
            String request,
            int managementDepth
    ) throws SQLException {
        ResultSet resultSet = ConnectionSource.instance().createConnection().createStatement().executeQuery(request);

        List<Employee> resultList = new ArrayList<>();

        while (resultSet.next()) {
            resultList.add(getEmployeeFromResultSet(resultSet, managementDepth));
        }
        return resultList;
    }

    private static Employee getEmployeeFromResultSet(ResultSet rs, int managementDepth) {
        try {
            BigInteger id = BigInteger.valueOf(rs.getInt("id"));

            String firstName = rs.getString("firstname");
            String lastName = rs.getString("lastname");
            String middleName = rs.getString("middlename");
            FullName fullName = new FullName(firstName, lastName, middleName);

            Position position = Position.valueOf(rs.getString("position"));

            LocalDate hired = rs.getDate("hiredate").toLocalDate();

            BigDecimal salary = rs.getBigDecimal("salary");

            //GETTING MANAGER
            Employee manager = null;
            int managerId = rs.getInt("manager");
            if (managerId != 0 && managementDepth < 1) {
                ResultSet managerResultSet = ConnectionSource.instance().createConnection().createStatement()
                        .executeQuery("SELECT * FROM EMPLOYEE WHERE id = " + managerId);
                //Recursion
                if (managerResultSet.next())
                    manager = getEmployeeFromResultSet(managerResultSet, managementDepth + 1);
            }

            //GETTING DEPARTMENT
            Department department = null;
            int departmentId = rs.getInt("department");
            if (departmentId != 0) {
                ResultSet departmentResultSet = ConnectionSource.instance().createConnection().createStatement()
                        .executeQuery("SELECT * FROM department WHERE id = " + departmentId);

                if (departmentResultSet.next())
                    department = getDepartmentFromResultSet(departmentResultSet);
            }
            return new Employee(id, fullName, position, hired, salary, manager, department);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Department getDepartmentFromResultSet(ResultSet rs) {
        try {
            BigInteger id = BigInteger.valueOf(Long.parseLong(rs.getString("ID")));
            String name = rs.getString("NAME");
            String location = rs.getString("LOCATION");

            return new Department(id, name, location);
        } catch (SQLException e) {
            return null;
        }
    }

    static BigInteger getDepartmentIdByName(String departmentName) throws SQLException {
        Department depId = null;

        String request = "SELECT * FROM department WHERE name = '" + departmentName + "'";

        ResultSet resultSet = ConnectionSource.instance().createConnection().createStatement().executeQuery(request);
        if (resultSet.next()) {
            depId = getDepartmentFromResultSet(resultSet);
        }
        return (depId == null ? BigInteger.valueOf(0) : depId.getId());
    }
}