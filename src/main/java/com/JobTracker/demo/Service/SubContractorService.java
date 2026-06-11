package com.JobTracker.demo.Service;

import com.JobTracker.demo.Entity.SubContractor;
import com.JobTracker.demo.Repository.SubContractorRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SubContractorService {

    private final SubContractorRepository subContractorRepository;

    public SubContractorService(SubContractorRepository subContractorRepository) {
        this.subContractorRepository = subContractorRepository;
    }

    public List<SubContractor> findAll() {
        return subContractorRepository.findAll();
    }

    public SubContractor findById(Long id) {
        return subContractorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("sub contractor not found"));
    }

    public SubContractor save(SubContractor subContractor) {
        return subContractorRepository.save(subContractor);
    }

    public void delete(Long subContractorId) {
        if(!subContractorRepository.existsById(subContractorId)) {
            throw new RuntimeException("sub contractor not found");
        }
        subContractorRepository.deleteById(subContractorId);
    }

    public SubContractor update(SubContractor subContractor, Long subContractorId) {

        SubContractor currentSubContractor = subContractorRepository.findById(subContractorId)
                .orElseThrow(() -> new RuntimeException("sub contractor not found"));

        currentSubContractor = subContractorRepository.findById(subContractorId).get();
        currentSubContractor.setCompanyName(subContractor.getCompanyName());
        currentSubContractor.setPrice(subContractor.getPrice());
        currentSubContractor.setPhoneNumber(subContractor.getPhoneNumber());

        return subContractorRepository.save(currentSubContractor);
    }

    public SubContractor updatePrice(BigDecimal price, Long subContractorId) {
        SubContractor currentSubContractor = subContractorRepository.findById(subContractorId)
                .orElseThrow(() -> new RuntimeException("sub contractor not found"));

        currentSubContractor.setPrice(price);
        return subContractorRepository.save(currentSubContractor);
    }





}
