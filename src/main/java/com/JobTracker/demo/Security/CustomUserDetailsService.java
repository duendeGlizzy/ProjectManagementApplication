package com.JobTracker.demo.Security;

import com.JobTracker.demo.Repository.AdminRepository;
import com.JobTracker.demo.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {


    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{

        var admin = adminRepository.findByEmail(email);
        if(admin.isPresent()){
            return admin.get();
        }

        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("no employee found with email: " +email));
    }
}
