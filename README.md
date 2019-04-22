# Spring Boot Online Banking Application

## Setup the Application

1. Create a database with schema named `demo`.

2. Open `src/main/resources/application.properties` and change `spring.datasource.username` and `spring.datasource.password` properties as per your MySQL installation.

3. Type `mvn spring-boot:run` from the root directory of the project to run the application.


4. End Points - 

   http://localhost:8080/accounts (will display all account and their transactions)
   
   ![Swagger Contracts](readmeDiagrams/Swagger1.png)
   
   ![Swagger Contracts](readmeDiagrams/Swagger2.png)
   
   http://localhost:8080/swagger-ui.html (Swagger End Points)
   ![Swagger Contracts](readmeDiagrams/Swagger3.png)
   
   
Assumptions:

1. Spring Security with OAuth could be added as a feature for Security/Authentication of Users

   


   