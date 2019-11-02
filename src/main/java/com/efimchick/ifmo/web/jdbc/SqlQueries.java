package com.efimchick.ifmo.web.jdbc;

/**
 * Implement sql queries like described
 */
public class SqlQueries {
    //Select all employees sorted by last name in ascending order
    //language=HSQLDB
    String select01 = "SELECT * FROM EMPLOYEE ORDER BY LASTNAME";

    //Select employees having no more than 5 characters in last name sorted by last name in ascending order
    //language=HSQLDB
    String select02 = "SELECT * FROM EMPLOYEE WHERE LENGTH(LASTNAME) <= 5 ORDER BY LASTNAME";

    //Select employees having salary no less than 2000 and no more than 3000
    //language=HSQLDB
    String select03 = "SELECT * FROM EMPLOYEE WHERE SALARY BETWEEN 2000 AND 3000";

    //Select employees having salary no more than 2000 or no less than 3000
    //language=HSQLDB
    String select04 = "SELECT * FROM EMPLOYEE WHERE SALARY <= 2000 OR SALARY >= 3000"; //<= 2000 OR >= Y3000

    //Select employees assigned to a department and corresponding department name
    //language=HSQLDB
    String select05 = "SELECT * FROM EMPLOYEE AS EMP INNER JOIN DEPARTMENT AS DP ON DP.ID = EMP.DEPARTMENT";

    //Select all employees and corresponding department name if there is one.
    //Name column containing name of the department "depname".
    //language=HSQLDB
    String select06 = "SELECT LASTNAME, SALARY, DP.NAME as depname FROM EMPLOYEE AS EMP LEFT JOIN DEPARTMENT AS DP ON DP.ID = EMP.DEPARTMENT";

    //Select total salary pf all employees. Name it "total".
    //language=HSQLDB
    String select07 = "SELECT SUM(SALARY) AS total FROM EMPLOYEE";

    //Select all departments and amount of employees assigned per department
    //Name column containing name of the department "depname".
    //Name column containing employee amount "staff_size".
    //language=HSQLDB
    String select08 = "SELECT dp.NAME AS depname, COUNT(emp.ID) AS staff_size " +
            "FROM EMPLOYEE emp INNER JOIN DEPARTMENT dp ON emp.DEPARTMENT = dp.ID " +
            "GROUP BY dp.NAME";

    //Select all departments and values of total and average salary per department
    //Name column containing name of the department "depname".
    //language=HSQLDB
    String select09 = "SELECT dp.NAME AS depname, SUM(emp.SALARY) AS total, AVG(emp.SALARY) AS average " +
            "FROM EMPLOYEE emp INNER JOIN DEPARTMENT dp ON emp.DEPARTMENT = dp.ID " +
            "GROUP BY dp.NAME";

    //Select all employees and their managers if there is one.
    //Name column containing employee lastname "employee".
    //Name column containing manager lastname "manager".
    //language=HSQLDB
    String select10 = "SELECT emp1.LASTNAME as employee, emp2.LASTNAME as manager FROM EMPLOYEE emp1 " +
            "LEFT JOIN EMPLOYEE emp2 ON emp1.MANAGER = emp2.ID";
    //"FROM EMPLOYEE emp1, EMPLOYEE emp2 WHERE emp1.MANAGER = emp2.ID";
    //"FROM EMPLOYEE emp1 " +
    //"WHERE (SELECT emp2.ID FROM EMPLOYEE emp2) = emp1.MANAGER OR emp1.MANAGER IS NULL";


}
