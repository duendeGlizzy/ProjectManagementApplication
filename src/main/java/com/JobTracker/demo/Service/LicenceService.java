package com.JobTracker.demo.Service;

import com.JobTracker.demo.Entity.Employee;
import com.JobTracker.demo.Entity.Licence;
import com.JobTracker.demo.Repository.EmployeeRepository;
import com.JobTracker.demo.Repository.LicenceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LicenceService {

    private final LicenceRepository licenceRepository;
    private final EmployeeRepository employeeRepository;

    public LicenceService(LicenceRepository licenceRepository,
                          EmployeeRepository employeeRepository) {
        this.licenceRepository = licenceRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Licence> findAllByEmployeeId(Long id) {
        return licenceRepository.findLicencesByEmployee_EmployeeId(id);
    }

    public Licence findById(Long id) {
        return licenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Licence not found"));
   }

    @Transactional
    public Licence createLicence(Licence licence, Long employeeId) {
        Licence newLicence = new Licence();
        Employee currentEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        newLicence.setEmployee(currentEmployee);

        newLicence.setName(licence.getName());
        newLicence.setExpirationDate(licence.getExpirationDate());
        newLicence.setIssueDate(licence.getIssueDate());

        return licenceRepository.save(newLicence);
    }


    public void deleteLicence(Long licenceId) {
        if(licenceRepository.existsById(licenceId)) {
            licenceRepository.deleteById(licenceId);
        }
    }

    public Licence updateLicence(Long licenceId, Licence licence) {
        Licence currentLicence = licenceRepository.findById(licenceId)
                .orElseThrow(() -> new RuntimeException("Licence not found"));

        currentLicence.setName(licence.getName());
        currentLicence.setExpirationDate(licence.getExpirationDate());
        currentLicence.setIssueDate(licence.getIssueDate());

        return licenceRepository.save(currentLicence);
    }



}
