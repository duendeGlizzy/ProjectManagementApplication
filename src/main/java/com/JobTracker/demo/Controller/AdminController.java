package com.JobTracker.demo.Controller;

import com.JobTracker.demo.Entity.Employee;
import com.JobTracker.demo.Service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }



    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = adminService.findAll();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/id")
    public ResponseEntity<Employee> getEmployeeById(@RequestParam Long id){
        Employee employee = adminService.findById(id);
        return ResponseEntity.ok(employee);
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee newEmployee = adminService.save(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(newEmployee);
    }

    @DeleteMapping("/id")
    public ResponseEntity<Void> deleteEmployee(@RequestParam Long id){
        adminService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/password")
    public ResponseEntity<Employee> updateEmployeePassword(@RequestParam Long id, @RequestBody Map<String, String> requestBody){
        String newPassword = requestBody.get("password");
        Employee updatedEmployee = adminService.updatePassword(id, newPassword);

        return ResponseEntity.ok(updatedEmployee);
    }


}
