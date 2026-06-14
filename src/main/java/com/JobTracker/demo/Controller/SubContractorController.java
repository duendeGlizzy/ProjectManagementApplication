package com.JobTracker.demo.Controller;

import com.JobTracker.demo.Entity.SubContractor;
import com.JobTracker.demo.Service.SubContractorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/sub-contractors")
public class SubContractorController {

    private final SubContractorService subContractorService;

    public SubContractorController(SubContractorService subContractorService) {
        this.subContractorService = subContractorService;
    }

    @GetMapping
    public ResponseEntity<List<SubContractor>> getAllSubContractors() {
        List<SubContractor> subContractors = subContractorService.findAll();
        return ResponseEntity.ok(subContractors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubContractor> getSubContractorById(@PathVariable Long id) {
        SubContractor subContractor = subContractorService.findById(id);
        return ResponseEntity.ok(subContractor);
    }

    @PostMapping
    public ResponseEntity<SubContractor> createSubContractor(@RequestBody SubContractor subContractor) {
        SubContractor subContractorCreated = subContractorService.save(subContractor);
        return ResponseEntity.status(HttpStatus.CREATED).body(subContractorCreated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubContractorById(@PathVariable Long id) {
        subContractorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubContractor> updateSubContractorById(@RequestBody SubContractor subContractor,@PathVariable Long id) {

        SubContractor currentSubContractor = subContractorService.update(subContractor, id);
        return ResponseEntity.ok(currentSubContractor);
    }

    @PutMapping("/{id}/updatePrice")
    public ResponseEntity<SubContractor> updateSubContractorPriceById(@RequestBody BigDecimal price, @PathVariable Long id) {

        SubContractor currentSubContractor = subContractorService.updatePrice(price, id);
        return ResponseEntity.ok(currentSubContractor);
    }




}
