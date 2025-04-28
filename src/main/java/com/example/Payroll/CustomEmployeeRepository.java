package com.example.Payroll;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomEmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT e FROM Employee e WHERE e.department = :departmentName")
    List<Employee> findByDepartmentName(@Param("departmentName") String departmentName);
}

