package com.JobTracker.demo.Service;

import com.JobTracker.demo.Entity.Employee;
import com.JobTracker.demo.Repository.AdminRepository;
import com.JobTracker.demo.Repository.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(EmployeeRepository employeeRepository,
                        PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Employee updatePassword(Long id, String newPassword) {
        Employee currentEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        String encodedPassword = passwordEncoder.encode(newPassword);

        currentEmployee.setPassword(encodedPassword);

        return employeeRepository.save(currentEmployee);
    }

    public void deleteById(Long id) {
        if(employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
        }
    }

    public Employee save(Employee employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        return employeeRepository.save(employee);
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }


}
