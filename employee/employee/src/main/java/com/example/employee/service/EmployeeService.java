package com.example.employee.service;

import com.example.employee.model.Employee;
import com.example.employee.model.TaxResponse;
import com.example.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public void addEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public TaxResponse calculateTax(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new IllegalArgumentException("Employee not found."));
        
        LocalDate current = LocalDate.now();
        LocalDate start = employee.getDateOfJoining().getMonthValue() <= 3 
                          ? employee.getDateOfJoining()
                          : LocalDate.of(current.getYear() - 1, 4, 1);
        LocalDate end = current.getMonthValue() >= 4
                        ? LocalDate.of(current.getYear(), 3, 31)
                        : LocalDate.of(current.getYear() - 1, 3, 31);

        int months = (int) ChronoUnit.MONTHS.between(start.withDayOfMonth(1), end.withDayOfMonth(1)) + 1;
        int daysInMonth = current.lengthOfMonth();
        int partialDays = start.getDayOfMonth() > 1 ? daysInMonth - start.getDayOfMonth() + 1 : daysInMonth;

        double dailySalary = employee.getMonthlySalary() / 30;
        double totalSalary = employee.getMonthlySalary() * months + partialDays * dailySalary;

        double tax = calculateTaxAmount(totalSalary);
        double cess = totalSalary > 2500000 ? (totalSalary - 2500000) * 0.02 : 0;

        return new TaxResponse(employee.getEmployeeId(), employee.getFirstName(), employee.getLastName(), 
                               totalSalary, tax, cess);
    }

    private double calculateTaxAmount(double yearlySalary) {
        double tax = 0;
        if (yearlySalary <= 250000) return tax;

        if (yearlySalary > 250000) tax += Math.min(yearlySalary - 250000, 250000) * 0.05;
        if (yearlySalary > 500000) tax += Math.min(yearlySalary - 500000, 500000) * 0.10;
        if (yearlySalary > 1000000) tax += (yearlySalary - 1000000) * 0.20;

        return tax;
    }
}