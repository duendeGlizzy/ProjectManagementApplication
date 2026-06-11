package com.JobTracker.demo.Service;

import com.JobTracker.demo.Entity.SubContractor;
import com.JobTracker.demo.Repository.SubContractorRepository;
import com.JobTracker.demo.Repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SubContractorService {

    private final SubContractorRepository subContractorRepository;
    private final TaskRepository taskRepository;

    public SubContractorService(SubContractorRepository subContractorRepository,
                                TaskRepository taskRepository) {
        this.subContractorRepository = subContractorRepository;
        this.taskRepository = taskRepository;

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
        if (taskRepository.existsBySubContractor_SubContractorId(subContractorId)) {
            throw new IllegalArgumentException("Cannot delete sub-contractor: They are currently assigned to active tasks.");
        }
        subContractorRepository.deleteById(subContractorId);
    }

    public SubContractor update(SubContractor subContractor, Long subContractorId) {

        SubContractor currentSubContractor = findById(subContractorId);

        if (subContractor.getCompanyName() != null && !subContractor.getCompanyName().isBlank()) {
            currentSubContractor.setCompanyName(subContractor.getCompanyName());
        }
        if (subContractor.getPhoneNumber() != null && !subContractor.getPhoneNumber().isBlank()) {
            currentSubContractor.setPhoneNumber(subContractor.getPhoneNumber());
        }
        if (subContractor.getPrice() != null) {
            currentSubContractor.setPrice(subContractor.getPrice());
        }

        return subContractorRepository.save(currentSubContractor);
    }

    public SubContractor updatePrice(BigDecimal price, Long subContractorId) {
        SubContractor currentSubContractor = subContractorRepository.findById(subContractorId)
                .orElseThrow(() -> new RuntimeException("sub contractor not found"));

        currentSubContractor.setPrice(price);
        return subContractorRepository.save(currentSubContractor);
    }





}
