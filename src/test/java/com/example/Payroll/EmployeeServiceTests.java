package com.example.Payroll;

import com.example.Payroll.Exceptions.EmployeeNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    public void testFindEmployeesByDepartment() {
        // Arrange
        String department = "Engineering";
        List<Employee> employeesInDepartment = Arrays.asList(
                new Employee(1L, "John Doe", "Software Engineer", 70000.0, "Engineering", LocalDate.of(2022, 3, 1)),
                new Employee(2L, "Jane Smith", "DevOps Engineer", 65000.0, "Engineering", LocalDate.of(2021, 6, 15))
        );
        when(employeeRepository.findByDepartmentName(department)).thenReturn(employeesInDepartment);

        // Act
        List<Employee> result = employeeService.findByDepartmentName(department);

        // Assert
        assertEquals(employeesInDepartment, result);
    }
    @Test
    public void testCalculateAverageSalaryByDepartment() {
        // Arrange
        String department = "Marketing";
        List<Employee> employeesInDepartment = Arrays.asList(
                new Employee(3L, "John Doe", "Marketing Manager", 80000.0, "Marketing", LocalDate.of(2020, 1, 1)),
                new Employee(4L, "Jane Smith", "Marketing Coordinator", 60000.0, "Marketing", LocalDate.of(2022, 9, 15))
        );
        when(employeeRepository.findByDepartmentName(department)).thenReturn(employeesInDepartment);

        // Act
        double averageSalary = employeeService.calculateAverageSalaryByDepartment(department);

        // Assert
        assertEquals(70000.0, averageSalary, 0.001);
    }
    @Test
    public void testGetAllEmployees() {
        // Arrange
        List<Employee> employees = Arrays.asList(
                new Employee(1L, "John Doe", "Manager", 80000.0, "Marketing", LocalDate.of(2020, 1, 1)),
                new Employee(2L, "Jane Smith", "Developer", 70000.0, "Engineering", LocalDate.of(2021, 3, 15))
        );
        when(employeeRepository.findAll()).thenReturn(employees);

        // Act
        List<Employee> result = employeeService.getAllEmployees();

        // Assert
        assertEquals(employees, result);
    }

    @Test
    public void testGetEmployeeById() {
        // Arrange
        Long employeeId = 1L;
        Employee employee = new Employee(employeeId, "John Doe", "Manager", 80000.0, "Marketing", LocalDate.of(2020, 1, 1));
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.ofNullable(employee));

        // Act
        Optional<Employee> result = employeeService.getEmployeeById(employeeId);

        // Assert
        assertEquals(employee, result);
    }

    @Test
    public void testGetEmployeeById_NotFound() {
        // Arrange
        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(employeeId));
    }

    @Test
    public void testCreateEmployee() {
        // Arrange
        Employee employee = new Employee(null, "John Doe", "Manager", 80000.0, "Marketing", LocalDate.of(2020, 1, 1));
        Employee savedEmployee = new Employee(1L, "John Doe", "Manager", 80000.0, "Marketing", LocalDate.of(2020, 1, 1));
        when(employeeRepository.save(employee)).thenReturn(savedEmployee);

        // Act
        Employee result = employeeService.createEmployee(employee);

        // Assert
        assertEquals(savedEmployee, result);
    }

    @Test
    public void testUpdateEmployee() {
        // Arrange
        Long employeeId = 1L;
        Employee existingEmployee = new Employee(employeeId, "John Doe", "Manager", 80000.0, "Marketing", LocalDate.of(2020, 1, 1));
        Employee updatedEmployee = new Employee(employeeId, "John Doe", "Senior Manager", 90000.0, "Marketing", LocalDate.of(2020, 1, 1));
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(employeeRepository.save(existingEmployee)).thenReturn(updatedEmployee);

        // Act
        Employee result = employeeService.updateEmployee(employeeId, updatedEmployee);

        // Assert
        assertEquals(updatedEmployee, result);
    }
    @Test
    public void testUpdateEmployee_NotFound() {
        // Arrange
        Long employeeId = 1L;
        Employee updatedEmployee = new Employee(employeeId, "John Doe", "Senior Manager", 90000.0, "Marketing", LocalDate.of(2020, 1, 1));
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.updateEmployee(employeeId, updatedEmployee));
    }
    @Test
    public void testDeleteEmployee() {
        // Arrange
        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(new Employee()));

        // Act
        employeeService.deleteEmployee(employeeId);

        // Assert
        verify(employeeRepository, times(1)).deleteById(employeeId);
    }
    @Test
    public void testDeleteEmployee_NotFound() {
        // Arrange
        Long employeeId = 1L;
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EmployeeNotFoundException.class, () -> employeeService.deleteEmployee(employeeId));
    }
}
