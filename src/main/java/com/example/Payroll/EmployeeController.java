package com.example.Payroll;
import com.example.Payroll.Exceptions.DepartmentNotFoundException;
import com.example.Payroll.Exceptions.EmployeeNotFoundException;
import com.example.Payroll.Exceptions.InvalidDataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        logger.info("GET /api/employees");
        List<Employee> employees = employeeService.getAllEmployees();
        logger.info("Retrieved {} employees", employees.size());
        return ResponseEntity.ok(employees);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Employee>> getEmployeeById(@PathVariable Long id) {
        logger.info("GET /api/employees/{}", id);
        try {
            Optional<Employee> employee = employeeService.getEmployeeById(id);
            logger.info("Retrieved employee: {}", employee);
            return ResponseEntity.ok(employee);
        } catch (EmployeeNotFoundException ex) {
            logger.error("Employee not found with id: {}", id, ex);
            return ResponseEntity.notFound().build();
        }
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
////        return employeeService.getEmployeeById(id)
////                .map(ResponseEntity::ok)
////                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));
//    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        logger.info("POST /api/employees");
        if(employee.getName()==null || employee.getRole()==null || employee.getSalary()==null){
            throw new InvalidDataException("Invalid data");
        }
        Employee createdEmployee = employeeService.createEmployee(employee);
        logger.info("Employee created");
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
        logger.info("PUT /api/employees/{}",id);
        Employee employee = employeeService.updateEmployee(id, updatedEmployee);
        if(employee!=null){
            logger.info("Employee details updated");
            return ResponseEntity.ok(employee);
        }else{
            logger.error("Employee not found with id: {}",id);
            return ResponseEntity.notFound().build();
        }
    }
    //        return employee != null ? ResponseEntity.ok(employee) : ResponseEntity.notFound().build();


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        logger.info("DELETE /api/employees/{}",id);
        employeeService.deleteEmployee(id);
        logger.info("Employee deleted");
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/payroll")
    public ResponseEntity<Double> calculatePayroll() {
        double totalPayroll = employeeService.calculatePayroll();
        return ResponseEntity.ok(totalPayroll);
    }
    @GetMapping("/department/{departmentName}/average-salary")
    public ResponseEntity<Double> getAverageSalaryByDepartment(@PathVariable String departmentName) {
        if(departmentName==null || departmentName.isEmpty()){
            throw new InvalidDataException("Invalid data for department name");
        }else{
            double averageSalary = employeeService.calculateAverageSalaryByDepartment(departmentName);
            return ResponseEntity.ok(averageSalary);
        }
    }
    @GetMapping("/grouped-by-department")
    public ResponseEntity<Map<String, List<Employee>>> getEmployeesGroupedByDepartment() {
        Map<String, List<Employee>> groupedEmployees = employeeService.getEmployeesGroupedByDepartment();
        if(groupedEmployees.isEmpty()){
            throw new IllegalArgumentException("No record found");
        }
        return ResponseEntity.ok(groupedEmployees);
    }
    @GetMapping("/top-salaries/{n}")
    public ResponseEntity<List<Employee>> getTopNHighestPaidEmployees(@PathVariable int n) {
        List<Employee> topEmployees = employeeService.getTopNHighestPaidEmployees(n);
        return ResponseEntity.ok(topEmployees);
    }
    @GetMapping("/payroll/job-title/{role}")
    public ResponseEntity<Double> calculatePayrollByJobTitle(@PathVariable String role) {
        double payroll = employeeService.calculatePayrollByJobTitle(role);
        return ResponseEntity.ok(payroll);
    }
    @GetMapping("/hired-in-last/{months}")
    public ResponseEntity<List<Employee>> findEmployeesHiredInLastNMonths(@PathVariable int months) {
        List<Employee> employees = employeeService.findEmployeesHiredInLastNMonths(months);
        return ResponseEntity.ok(employees);
    }
}
