package com.JobTracker.demo.Repository;

import com.JobTracker.demo.Entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
