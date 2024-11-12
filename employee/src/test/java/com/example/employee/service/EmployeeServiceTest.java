package com.example.employee.service;

import com.example.employee.model.Employee;
import com.example.employee.model.TaxResponse;
import com.example.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addEmployee_ShouldSaveEmployee() {
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setDateOfJoining(LocalDate.of(2023, 5, 1));
        employee.setMonthlySalary(50000.00);

        employeeService.addEmployee(employee);

        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void calculateTax_ShouldReturnTaxResponse_ForValidEmployee() {
        Employee employee = new Employee();
        employee.setEmployeeId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setDateOfJoining(LocalDate.of(2023, 5, 1));
        employee.setMonthlySalary(50000.00);
        
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        TaxResponse response = employeeService.calculateTax(1L);

        assertEquals(1L, response.getEmployeeId());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals(550000.00, response.getYearlySalary(), 1);
        assertEquals(37500.00, response.getTaxAmount(), 1);
        assertEquals(0.0, response.getCessAmount(), 1);
    }

    @Test
    void calculateTax_ShouldThrowException_ForInvalidEmployeeId() {
        when(employeeRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> employeeService.calculateTax(99L));
    }
}