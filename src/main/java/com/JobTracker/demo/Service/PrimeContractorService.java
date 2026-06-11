package com.JobTracker.demo.Service;

import com.JobTracker.demo.Entity.PrimeContractor;
import com.JobTracker.demo.Repository.PrimeContractorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrimeContractorService {

    private final PrimeContractorRepository primeContractorRepository;

    public PrimeContractorService(PrimeContractorRepository primeContractorRepository) {
        this.primeContractorRepository = primeContractorRepository;
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
        primeContractorRepository.deleteById(id);
    }

    public PrimeContractor update(Long id, PrimeContractor primeContractor) {
        if(!primeContractorRepository.existsById(id)) {
            throw new RuntimeException("Prime Contractor not found");
        }
        PrimeContractor currentContractor = findById(id);

        currentContractor.setAddress(primeContractor.getAddress());
        currentContractor.setCompanyName(primeContractor.getCompanyName());
        currentContractor.setPhoneNumber(primeContractor.getPhoneNumber());

        return primeContractorRepository.save(currentContractor);
    }




}
