# BFH Qualifier - Spring Boot Submission

## Overview
This Spring Boot application solves the Bajaj Finserv Health Qualifier 1:

1. Generates a webhook using the provided registration details.
2. Computes the SQL query for the problem:
   - Count of employees younger than each employee in their department.
3. Submits the solution to the webhook automatically using JWT authentication.

## How to Run

1. Clone the repository:

##Build the JAR:

mvn clean package


Run the application:

java -jar target/bfh-qualifier-0.0.1-SNAPSHOT.jar


The app will print webhook URL and token.

It will automatically submit the SQL query to the webhook.

SQL Query Used
SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME,
       COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT
FROM EMPLOYEE e1
JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID
LEFT JOIN EMPLOYEE e2 ON e1.DEPARTMENT = e2.DEPARTMENT AND e2.DOB > e1.DOB
GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME
ORDER BY e1.EMP_ID DESC;
