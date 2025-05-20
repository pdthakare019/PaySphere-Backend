# PaySphere (Payroll System Backend)

<div align="center">
  <img src="https://raw.githubusercontent.com/github/explore/80688e429a7d4ef2fca1e82350fe8e3517d3494d/topics/java/java.png" alt="Java Logo" width="80" height="80">
  <img src="https://raw.githubusercontent.com/github/explore/80688e429a7d4ef2fca1e82350fe8e3517d3494d/topics/spring-boot/spring-boot.png" alt="Spring Boot Logo" width="80" height="80">
  <img src="https://raw.githubusercontent.com/github/explore/80688e429a7d4ef2fca1e82350fe8e3517d3494d/topics/postgresql/postgresql.png" alt="PostgreSQL Logo" width="80" height="80">
</div>

The Payroll System is a Spring Boot application designed to manage employee records and process payroll calculations. It provides a RESTful API for performing CRUD operations on employee data and calculating payroll details based on employee roles and salaries.

## Features

- **Employee Management**: Create, read, update, and delete employee records.
- **Payroll Processing**: Calculate payroll for employees, including base salaries, bonuses, and total payroll amounts.
- **Data Persistence**: Store employee data in a PostgreSQL database for persistent storage.
- **Exception Handling**: Centralized exception handling with consistent error responses.
- **Logging**: Comprehensive logging for tracking application actions and debugging.
- **Integration Testing**: Integration tests to validate the functionality of the RESTful API endpoints.
- **Unit Testing**: Unit tests for service layer components and utility classes.

## Technologies Used

- **Java**: The primary programming language used for the application.
- **Spring Boot**: The framework used for building the RESTful API and managing dependencies.
- **PostgreSQL**: The database management system used for persistent data storage.
- **JUnit**: The testing framework used for writing unit tests.
- **Mockito**: The mocking framework used for creating mock objects in unit tests.

## Getting Started

### Prerequisites

- Java 8 or later
- PostgreSQL installed and running

### Setup

1. Clone the repository.
2. Navigate to the project directory.
3. Create a new PostgreSQL database for the application.
4. Update the `src/main/resources/application.properties` file with your PostgreSQL database connection, JPA and Logging Configuration details.
