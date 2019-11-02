package com.efimchick.ifmo.web.jdbc;

import com.efimchick.ifmo.web.jdbc.domain.Employee;
import com.efimchick.ifmo.web.jdbc.domain.FullName;
import com.efimchick.ifmo.web.jdbc.domain.Position;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class RowMapperFactory {

    public RowMapper<Employee> employeeRowMapper() {
        RowMapper<Employee> rw = new RowMapper() {
            @Override
            public Employee mapRow(ResultSet rs) {
                try {
                    BigInteger id = BigInteger.valueOf(rs.getInt("id"));

                    String firstName = rs.getString("firstname");
                    String lastName = rs.getString("lastname");
                    String middleName = rs.getString("middlename");
                    FullName fullName = new FullName(firstName, lastName, middleName);

                    Position position = Position.valueOf(rs.getString("position"));

                    LocalDate hired = rs.getDate("hiredate").toLocalDate();

                    BigDecimal salary = rs.getBigDecimal("salary");

                    return new Employee(id, fullName, position, hired, salary);
                } catch (SQLException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
        return rw;
    }
}
