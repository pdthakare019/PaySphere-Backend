package com.example.Payroll;
import com.example.Payroll.Exceptions.DepartmentNotFoundException;
import com.example.Payroll.Exceptions.EmployeeNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    private static final Logger logger = LoggerFactory.getLogger((EmployeeService.class));

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        logger.debug("Getting all employees");
        List<Employee>list =  employeeRepository.findAll();
        logger.debug("Retrieved {} employees", list.size());
        return list;
    }

    public Optional<Employee> getEmployeeById(Long id) {
        logger.debug("Getting employee by id: {}",id);
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if(optionalEmployee.isPresent()){
            logger.debug("Found employee: {}",optionalEmployee.get());
            return optionalEmployee;
        }else{
            logger.warn("Employee not found with id: {}",id);
            throw new EmployeeNotFoundException("Employee not found");
        }
//        return Optional.ofNullable(optionalEmployee.orElse(null));
    }

    public Employee createEmployee(Employee employee) {
        logger.debug("Creating new employee");
        logger.debug("Employee created!!");
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        logger.debug("Updating employee details with id: {}",id);
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee existingEmployee = optionalEmployee.get();
            existingEmployee.setName(updatedEmployee.getName());
            existingEmployee.setRole(updatedEmployee.getRole());
            existingEmployee.setSalary(updatedEmployee.getSalary());
            existingEmployee.setHiringDate(updatedEmployee.getHiringDate());
            logger.debug("Employee details updated");
            return employeeRepository.save(existingEmployee);
        } else {
            logger.warn("No employee record found with id: {}",id);
            return null;
        }
    }

    public void deleteEmployee(Long id) {
        logger.debug("Employee deleted with id: {}",id);
        employeeRepository.deleteById(id);
    }
    //base salary and bonus
    public double calculatePayroll() {
        List<Employee> employees = employeeRepository.findAll();
        double totalPayroll = 0;
        if(!employees.isEmpty()){
            for (Employee employee : employees) {
                if(employee!=null) {
                    double baseSalary = getBaseSalaryForRole(employee.getRole());
                    double bonus = calculateBonus(employee.getSalary(), baseSalary);
                    totalPayroll += employee.getSalary() + bonus;
                }else{
                    throw new EmployeeNotFoundException("Null employee");
                }
            }
        }else{
            totalPayroll = 0;
        }
        return totalPayroll;
    }

    private double getBaseSalaryForRole(String role) {
        switch (role.toLowerCase()) {
            case "manager":
                return 80000;
            case "developer":
                return 60000;
            case "intern":
                return 30000;
            default:
                return 50000; // Default base salary
        }
    }

    private double calculateBonus(double salary, double baseSalary) {
        double bonusPercentage;
        if (salary > 1.2 * baseSalary) {
            bonusPercentage = 0.1; // 10% bonus
        } else {
            bonusPercentage = 0.05; // 5% bonus
        }
        return salary * bonusPercentage;
    }
    public List<Employee> findByDepartmentName(String departmentName){
        return employeeRepository.findByDepartmentName(departmentName);
    }
    public double calculateAverageSalaryByDepartment(String departmentName) {
        List<Employee> employeesInDepartment = employeeRepository.findByDepartmentName(departmentName);

        if (employeesInDepartment.isEmpty()) {
            throw new DepartmentNotFoundException("Department not found: " + departmentName);
        }

        return employeesInDepartment.stream()
                .mapToDouble(Employee::getSalary)
                .average()
                .orElse(0.0);
    }
    public Map<String, List<Employee>> getEmployeesGroupedByDepartment() {
        List<Employee> employees = employeeRepository.findAll();
        if(employees.isEmpty()){
            throw new EmployeeNotFoundException(" Employee not found");
        }
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment));
    }
    public List<Employee> getTopNHighestPaidEmployees(int n) {
        List<Employee> employees = employeeRepository.findAll();
        if(employees.isEmpty()){
            throw new EmployeeNotFoundException(" Employee not found");
        }
        return employees.stream()
                .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }
    public double calculatePayrollByJobTitle(String role) {
        List<Employee> employees = employeeRepository.findAll();
        if(employees.isEmpty()){
            throw new EmployeeNotFoundException("Employee not found");
        }
        return employees.stream()
                .filter(e -> e.getRole().equalsIgnoreCase(role))
                .mapToDouble(Employee::getSalary)
                .sum();
    }
    public List<Employee> findEmployeesHiredInLastNMonths(int months) {
        List<Employee> employees = employeeRepository.findAll();
        LocalDate currentDate = LocalDate.now();
        LocalDate cutoffDate = currentDate.minusMonths(months);

        return employees.stream()
                .filter(e -> e.getHiringDate().isAfter(cutoffDate))
                .collect(Collectors.toList());
    }
}