package com.JobTracker.demo.Repository;

import com.JobTracker.demo.Entity.Licence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LicenceRepository extends JpaRepository<Licence, Long> {
}
