package com.efimchick.ifmo.web.jdbc.service;

import com.efimchick.ifmo.web.jdbc.domain.Department;
import com.efimchick.ifmo.web.jdbc.domain.Employee;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceFactory {

    private List<Employee> getListEmploye(String request, String sort, Paging paging) {
        try {
//            Iterable<List<Employee>> result = new ArrayList<>();

            return ServiceUtils.getEmployeeResultListByRequest(
                    ServiceUtils.makePagingSQLRequest(request, sort, paging),
                    0
            );
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    public EmployeeService employeeService(){
        return new EmployeeService() {
            @Override
            public List<Employee> getAllSortByHireDate(Paging paging) {
                return getListEmploye("SELECT * FROM EMPLOYEE", "HIREDATE", paging);
            }

            @Override
            public List<Employee> getAllSortByLastname(Paging paging) {
                return getListEmploye("SELECT * FROM EMPLOYEE","LASTNAME", paging);
            }

            @Override
            public List<Employee> getAllSortBySalary(Paging paging) {
                return getListEmploye("SELECT * FROM EMPLOYEE","SALARY", paging);
            }

            @Override
            public List<Employee> getAllSortByDepartmentNameAndLastname(Paging paging) {
                return getListEmploye("SELECT * FROM EMPLOYEE","DEPARTMENT, LASTNAME", paging);
            }

            @Override
            public List<Employee> getByDepartmentSortByHireDate(Department department, Paging paging) {
                return getListEmploye("SELECT * FROM EMPLOYEE WHERE DEPARTMENT = " + department.getId().toString(), "HIREDATE", paging);
            }

            @Override
            public List<Employee> getByDepartmentSortBySalary(Department department, Paging paging) {
                return getListEmploye("SELECT * FROM EMPLOYEE WHERE DEPARTMENT = " + department.getId(),"SALARY", paging);
            }

            @Override
            public List<Employee> getByDepartmentSortByLastname(Department department, Paging paging) {
                return getListEmploye("SELECT * FROM EMPLOYEE WHERE DEPARTMENT = " + department.getId(), "LASTNAME", paging);
            }

            @Override
            public List<Employee> getByManagerSortByLastname(Employee manager, Paging paging) {
                return getListEmploye("SELECT * FROM EMPLOYEE WHERE MANAGER = " + manager.getId(),"LASTNAME", paging);
            }

            @Override
            public List<Employee> getByManagerSortByHireDate(Employee manager, Paging paging) {
                return getListEmploye("SELECT * FROM EMPLOYEE WHERE MANAGER = " + manager.getId(),"HIREDATE", paging);
            }

            @Override
            public List<Employee> getByManagerSortBySalary(Employee manager, Paging paging) {
                return getListEmploye("SELECT * FROM EMPLOYEE WHERE MANAGER = " + manager.getId() ,"SALARY", paging);
            }

            @Override
            public Employee getWithDepartmentAndFullManagerChain(Employee employee) {
                try {
                    return ServiceUtils.getEmployeeResultListByRequest(
                            "SELECT * FROM EMPLOYEE WHERE id = " + employee.getId(),
                            -5
                    ).get(0);
                } catch (SQLException e) {
                    System.out.println(e);
                    return null;
                }
            }

            @Override
            public Employee getTopNthBySalaryByDepartment(int salaryRank, Department department) {
                try {
                    return ServiceUtils.getEmployeeResultListByRequest(
                            "SELECT * FROM employee WHERE department = " + department.getId() + "ORDER BY salary DESC",
                            0
                    ).get(salaryRank - 1);
                } catch (SQLException e) {
                    System.out.println(e);
                    return null;
                }
            }
        };
    }
}
