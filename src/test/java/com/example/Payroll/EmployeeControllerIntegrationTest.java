package com.example.Payroll;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @Test
    public void testGetAllEmployees() throws Exception {
        List<Employee> employees = Arrays.asList(
                new Employee(1L, "John Doe", "Manager", 80000.0, "Marketing", LocalDate.of(2020, 1, 1)),
                new Employee(2L, "Jane Smith", "Developer", 70000.0, "Engineering", LocalDate.of(2021, 3, 15))
        );

        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[1].role", is("Developer")));
    }

    @Test
    public void testGetEmployeesGroupedByDepartment() throws Exception {
        Map<String, List<Employee>> groupedEmployees = new HashMap<>();
        groupedEmployees.put("Marketing", Arrays.asList(
                new Employee(1L, "John Doe", "Manager", 80000.0, "Marketing", LocalDate.of(2020, 1, 1))
        ));
        groupedEmployees.put("Engineering", Arrays.asList(
                new Employee(2L, "Jane Smith", "Developer", 70000.0, "Engineering", LocalDate.of(2021, 3, 15)),
                new Employee(3L, "Bob Johnson", "DevOps Engineer", 65000.0, "Engineering", LocalDate.of(2022, 6, 1))
        ));

        when(employeeService.getEmployeesGroupedByDepartment()).thenReturn(groupedEmployees);

        mockMvc.perform(get("/api/employees/grouped-by-department"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.Marketing[0].name", is("John Doe")))
                .andExpect(jsonPath("$.Engineering", hasSize(2)))
                .andExpect(jsonPath("$.Engineering[1].role", is("DevOps Engineer")));
    }

}
