package com.example.Payroll;
import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "employees")
public  class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String role;
    private Double salary;
    private String department;
    private LocalDate hiringDate;


    public Employee() {
    }

    public Employee(Long id, String name, String role, Double salary, String department, LocalDate hiringDate) {

        this.id = id;
        this.name = name;
        this.role = role;
        this.salary = salary;
        this.department = department;
        this.hiringDate = hiringDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
    public String getDepartment(){
        return department;
    }
    public void setDepartment(String s){
        this.department = s;
    }
    public LocalDate getHiringDate() {
        return hiringDate;
    }
    public void setHiringDate(LocalDate hiringDate) {
        this.hiringDate = hiringDate;
    }
}