package com.JobTracker.demo.Service;

import com.JobTracker.demo.Entity.PrimeContractor;
import com.JobTracker.demo.Repository.JobRepository;
import com.JobTracker.demo.Repository.PrimeContractorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrimeContractorService {

    private final PrimeContractorRepository primeContractorRepository;
    private final JobRepository jobRepository;

    public PrimeContractorService(PrimeContractorRepository primeContractorRepository, JobRepository jobRepository) {
        this.primeContractorRepository = primeContractorRepository;
        this.jobRepository = jobRepository;
    }

    public List<PrimeContractor> findAll() {
        return primeContractorRepository.findAll();
    }

    public PrimeContractor findById(Long id) {
        return primeContractorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prime Contractor not found"));
    }

    public PrimeContractor save(PrimeContractor primeContractor) {
        return primeContractorRepository.save(primeContractor);
    }

    public void deleteById(Long id) {
        if(!primeContractorRepository.existsById(id)) {
            throw new RuntimeException("Prime Contractor not found");
        }
        if(jobRepository.existsByPrimeContractor_PrimeContractorId(id)){
            throw new IllegalArgumentException("Cannot delete contractor: They are currently assigned to active jobs.");        }
        primeContractorRepository.deleteById(id);
    }

    public PrimeContractor update(Long id, PrimeContractor primeContractor) {
        PrimeContractor currentContractor = findById(id);

        if (primeContractor.getCompanyName() != null && !primeContractor.getCompanyName().isBlank()) {
            currentContractor.setCompanyName(primeContractor.getCompanyName());
        }
        if (primeContractor.getAddress() != null && !primeContractor.getAddress().isBlank()) {
            currentContractor.setAddress(primeContractor.getAddress());
        }
        if (primeContractor.getPhoneNumber() != null && !primeContractor.getPhoneNumber().isBlank()) {
            currentContractor.setPhoneNumber(primeContractor.getPhoneNumber());
        }

        return primeContractorRepository.save(currentContractor);
    }






}
