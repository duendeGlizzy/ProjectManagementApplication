package com.JobTracker.demo.Repository;

import com.JobTracker.demo.Entity.Licence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LicenceRepository extends JpaRepository<Licence, Long> {

    List<Licence> findLicencesByEmployee_EmployeeId(Long employeeId);
}
