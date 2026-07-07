package com.JobTracker.demo.Controller;

import com.JobTracker.demo.DTO.LoginRequest;
import com.JobTracker.demo.Entity.Admin;
import com.JobTracker.demo.Entity.Employee;
import com.JobTracker.demo.Repository.AdminRepository;
import com.JobTracker.demo.Repository.EmployeeRepository;
import com.JobTracker.demo.Security.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired
    private AdminRepository adminRepository;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateToken(authentication);

        Cookie jwtCookie = new Cookie("auth_token", jwtToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24*60*60);

        response.addCookie(jwtCookie);

        return ResponseEntity.ok(Map.of(
                "email", authentication.getName(),
                "role", "EMPLOYEE",
                "message", "logged in"
        ));
    }

    @PostMapping("/login/admin")
    public ResponseEntity<?> authenticateAdmin(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        Admin admin = adminRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        if(admin == null || !passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid username or password"));
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(admin, null, admin.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = jwtUtils.generateToken(authentication);

        Cookie jwtCookie = new Cookie("auth_token", jwtToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24*60*60);
        response.addCookie(jwtCookie);

        return ResponseEntity.ok(Map.of(
                "email", admin.getEmail(),
                "role", "ADMIN",
                "message", "logged in"
        ));
    }


}
