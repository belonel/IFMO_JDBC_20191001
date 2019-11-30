package com.efimchick.ifmo.web.jdbc.dao;

import com.efimchick.ifmo.web.jdbc.ConnectionSource;
import com.efimchick.ifmo.web.jdbc.domain.Department;
import com.efimchick.ifmo.web.jdbc.domain.Employee;
import com.efimchick.ifmo.web.jdbc.domain.FullName;
import com.efimchick.ifmo.web.jdbc.domain.Position;
//import jdk.jfr.events.ExceptionThrownEvent;

//import java.awt.print.PrinterAbortException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DaoFactory {

    private Department getDepartmentFromRow(ResultSet rs) {
        try {
            BigInteger id = BigInteger.valueOf(rs.getInt("id"));
            String name = rs.getString("name");
            String location = rs.getString("location");

            return new Department(id, name, location);
        }
        catch (SQLException e){
            System.out.println("get Department From result Set FAILED");
            return null;
        }
    }


    private List<Department> getDepartmentByRequest(String request) {
        try {
            ResultSet rs = executeSqlString(request);
            List<Department> departments = new ArrayList<>();

            while (rs.next()) {
                departments.add(getDepartmentFromRow(rs));

            }
            return departments;
        }
        catch (SQLException e) {
            System.out.println("get Employees by SQL Request Failed");
            return null;
        }
    }

    /*private boolean deleteDepartmentWithTheSameId (Department department) {
        String request = "";
        executeSqlString(request);
        return true;
    }*/
    //------------

    private Employee getEmployeeFromRow(ResultSet rs) {
        try {
            BigInteger id = BigInteger.valueOf(rs.getInt("id"));

            String firstName = rs.getString("firstname");
            String lastName = rs.getString("lastname");
            String middleName = rs.getString("middlename");
            FullName fullName = new FullName(firstName, lastName, middleName);

            Position position = Position.valueOf(rs.getString("position"));

            LocalDate hired = rs.getDate("hiredate").toLocalDate();

            BigDecimal salary = rs.getBigDecimal("salary");

            BigInteger managerId = BigInteger.valueOf(rs.getInt("manager"));

            BigInteger departmentId = BigInteger.valueOf(rs.getInt("department"));

            return new Employee(id, fullName, position, hired, salary, managerId, departmentId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Employee> getEmployeesByRequest(String request) {
        try {
            ResultSet rs = executeSqlString(request);
            List<Employee> employees = new ArrayList<>();

            while (rs.next()) {
                employees.add(getEmployeeFromRow(rs));

            }
            return employees;
        }
        catch (SQLException e) {
            System.out.println("get Employees by SQL Request Failed");
            return null;
        }
    }

    private ResultSet executeSqlString(String request) {
        try {
            return ConnectionSource.instance().createConnection().createStatement().executeQuery(request);
        }
        catch (SQLException e) {
            System.out.println("EXECUTE sql String request Failed");
            return null;
        }
    }
    //------------------------------------
    public EmployeeDao employeeDAO() {
        return new EmployeeDao() {
            @Override
            public List<Employee> getByDepartment(Department department) {
                return getEmployeesByRequest("SELECT * FROM employee WHERE department = " + department.getId());
            }

            @Override
            public List<Employee> getByManager(Employee employee) {
                return getEmployeesByRequest("SELECT * FROM employee WHERE manager = " + employee.getId());
            }

            @Override
            public Optional<Employee> getById(BigInteger Id) {
                try {
                    return Optional.of(getEmployeesByRequest("SELECT * FROM employee WHERE id = " + Id.toString()).get(0));
                }
                catch (Exception e) {
                    return Optional.empty();
                }
            }

            @Override
            public List<Employee> getAll() {
                return getEmployeesByRequest("SELECT * FROM employee");
            }

            @Override
            public Employee save(Employee employee) {
                try (Connection con = ConnectionSource.instance().createConnection()){
                    String request = "INSERT INTO employee VALUES (?,?,?,?,?,?,?,?,?)";
                    PreparedStatement prepStat = con.prepareStatement(request);

                    prepStat.setInt(1, employee.getId().intValue());
                    prepStat.setString(2, employee.getFullName().getFirstName());
                    prepStat.setString(3, employee.getFullName().getLastName());
                    prepStat.setString(4, employee.getFullName().getMiddleName());
                    prepStat.setString(5, employee.getPosition().toString());
                    prepStat.setInt(6, employee.getManagerId().intValue());
                    prepStat.setDate(7,  Date.valueOf(employee.getHired()));
                    prepStat.setDouble(8,
                            employee.getSalary().doubleValue());
                    prepStat.setInt(9, employee.getDepartmentId().intValue());

                    prepStat.executeUpdate();
                    return employee;
                }
                catch (SQLException e) {
                    System.out.println("Employee save failed");
                    return null;
                }
            }

            @Override
            public void delete(Employee employee) {
                try (Connection con = ConnectionSource.instance().createConnection()) {
                    String request = "DELETE FROM EMPLOYEE WHERE ID=?";
                    PreparedStatement prepStat = con.prepareStatement(request);

                    prepStat.setInt(1, employee.getId().intValue());
                    prepStat.executeUpdate();
                }
                catch (SQLException e) {
                    System.out.println("Delete employee failed.");
                }
            }
        };
    }

    //----------------------------------------------

    //---------------------------------------
    public DepartmentDao departmentDAO() {
        return new DepartmentDao() {
            @Override
            public Optional<Department> getById(BigInteger Id) {
                try {
                    return Optional.of(getDepartmentByRequest("SELECT * FROM department WHERE id = " + Id.toString()).get(0));
                }
                catch (Exception e) {
                    return Optional.empty();
                }
            }

            @Override
            public List<Department> getAll() {
                return getDepartmentByRequest("SELECT * FROM department");
            }

            @Override
            public Department save(Department department) {
                try (Connection con = ConnectionSource.instance().createConnection()) {
                    String request = "INSERT INTO department VALUES (?,?,?)";
                    PreparedStatement prepStat = con.prepareStatement(request);

                    prepStat.setInt(1,department.getId().intValue());
                    prepStat.setString(2, department.getName());
                    prepStat.setString(3, department.getLocation());

                    executeSqlString("DELETE FROM department WHERE id = " + department.getId().toString());
                    prepStat.executeUpdate();

                    return department;
                }
                catch (Exception e) {
                    System.out.println("Save department to table FAILED");
                    return null;
                }
            }

            @Override
            public void delete(Department department) {
                try(Connection con = ConnectionSource.instance().createConnection()) {
                    String request = "DELETE FROM department WHERE id=?";
                    PreparedStatement prepStat = con.prepareStatement(request);

                    prepStat.setInt(1,department.getId().intValue());

                    prepStat.executeUpdate();
                }
                catch (SQLException e) {
                    System.out.println("Delete Department Failed");
                }
            }
        };
    }
}
