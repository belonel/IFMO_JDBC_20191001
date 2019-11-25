package com.efimchick.ifmo.web.jdbc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;

import com.efimchick.ifmo.web.jdbc.domain.Employee;
import com.efimchick.ifmo.web.jdbc.domain.FullName;
import com.efimchick.ifmo.web.jdbc.domain.Position;

public class SetMapperFactory {

    public SetMapper<Set<Employee>> employeesSetMapper() {
        //---
        return new SetMapper<Set<Employee>> (){
            @Override
            public Set<Employee> mapSet(ResultSet resultSet) {
                Set<Employee> setOfEmployes = new HashSet<>();
                try {
                    while(resultSet.next()) {
                        Employee employee = getEmployee(resultSet);
                        setOfEmployes.add(employee);
                    }
                }
                catch (SQLException sql) {
                    System.out.println(sql.getErrorCode());
                    System.out.println(sql.getMessage());
                }
                return setOfEmployes;
            } //end of mapSet method
        //---
        };
    }

    private Employee getEmployee(ResultSet rs) {
        try {
            BigInteger id = BigInteger.valueOf(rs.getInt("id"));

            String firstName = rs.getString("firstname");
            String lastName = rs.getString("lastname");
            String middleName = rs.getString("middlename");
            FullName fullName = new FullName(firstName, lastName, middleName);

            Position position = Position.valueOf(rs.getString("position"));

            LocalDate hired = rs.getDate("hiredate").toLocalDate();

            BigDecimal salary = rs.getBigDecimal("salary");

            Employee manager = null;
            int managerId = rs.getInt("manager");
            //Если не 0 (у работника есть менеджер), то найдем менеджера и преобразуем его в Employee
            if (managerId != 0) {
                int current = rs.getRow();
                rs.beforeFirst();
                while (rs.next()) {
                    if (rs.getInt("ID") == managerId) {
                        manager = getEmployee(rs);
                    }
                }
                rs.absolute(current);

            }

            return new Employee(id, fullName, position, hired, salary, manager);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}