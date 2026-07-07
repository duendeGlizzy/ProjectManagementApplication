package com.JobTracker.demo.Service;

import com.JobTracker.demo.Entity.Employee;
import com.JobTracker.demo.Repository.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }



    public Employee update(Employee employee, Long id) {
            Employee currentEmployee = employeeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            currentEmployee.setEmail(employee.getEmail());
            currentEmployee.setFirstName(employee.getFirstName());
            currentEmployee.setLastName(employee.getLastName());

            return employeeRepository.save(currentEmployee);
    }

    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }





}
