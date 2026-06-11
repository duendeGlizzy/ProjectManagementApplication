package com.JobTracker.demo.Repository;


import com.JobTracker.demo.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
