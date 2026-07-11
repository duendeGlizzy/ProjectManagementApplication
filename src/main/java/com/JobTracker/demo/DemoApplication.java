package com.JobTracker.demo;

import com.JobTracker.demo.Entity.Admin;
import com.JobTracker.demo.Repository.AdminRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DemoApplication {

	@Value("${ADMIN_PASS:password!}")
	private String adminPassword;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner initAdminAccount(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
		return args -> {

			if (adminRepository.count() == 0) {
				Admin admin = new Admin();
				admin.setEmail("admin@jobtracker.com");

				admin.setPassword(passwordEncoder.encode(adminPassword));

				adminRepository.save(admin);
			}


		};
	}


}



