package com.example.employee.controller;

import com.example.employee.model.Employee;
import com.example.employee.model.TaxResponse;
import com.example.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
@Validated
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public String addEmployee(@Valid @RequestBody Employee employee) {
        employeeService.addEmployee(employee);
        return "Employee added successfully";
    }

    @GetMapping("/{id}/tax")
    public TaxResponse getTaxDeduction(@PathVariable Long id) {
        return employeeService.calculateTax(id);
    }
}