package com.JobTracker.demo.Controller;

import com.JobTracker.demo.Entity.Employee;
import com.JobTracker.demo.Service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @GetMapping("/me")
    public ResponseEntity<Employee> getLoggedInEmployee(@AuthenticationPrincipal UserDetails userDetails) {
        String currentEmail = userDetails.getUsername();

        Employee currentEmployee = employeeService.findByEmail(currentEmail);

        return ResponseEntity.ok(currentEmployee);

    }

    @PutMapping("/me")
    public ResponseEntity<Employee> updateEmployee(@RequestBody Employee employee, @AuthenticationPrincipal UserDetails userDetails) {

        Employee currentEmployee = employeeService.findByEmail(userDetails.getUsername());

        Employee updatedEmployee = employeeService.update(employee, currentEmployee.getEmployeeId());

        return ResponseEntity.ok(updatedEmployee);
    }








}
