package com.example.employee.controller;

import com.example.employee.model.Employee;
import com.example.employee.model.TaxResponse;
import com.example.employee.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addEmployee_ShouldReturnCreatedStatus() throws Exception {
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setDateOfJoining(LocalDate.of(2023, 5, 1));
        employee.setMonthlySalary(50000.00);

        mockMvc.perform(post("/api/employees/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated());
    }

    @Test
    void calculateTax_ShouldReturnTaxResponse_ForValidEmployee() throws Exception {
        TaxResponse taxResponse = new TaxResponse(1L, "John", "Doe", 550000.00, 37500.00, 0.0);
        
        Mockito.when(employeeService.calculateTax(eq(1L))).thenReturn(taxResponse);

        mockMvc.perform(get("/api/employees/1/tax")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.yearlySalary").value(550000.00))
                .andExpect(jsonPath("$.taxAmount").value(37500.00))
                .andExpect(jsonPath("$.cessAmount").value(0.0));
    }

    @Test
    void calculateTax_ShouldReturnNotFound_ForInvalidEmployeeId() throws Exception {
        Mockito.when(employeeService.calculateTax(any(Long.class))).thenThrow(new IllegalArgumentException("Employee not found."));

        mockMvc.perform(get("/api/employees/99/tax")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}