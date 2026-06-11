package com.JobTracker.demo.Controller;

import com.JobTracker.demo.Entity.PrimeContractor;
import com.JobTracker.demo.Service.PrimeContractorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prime_contractors")
public class PrimeContractorController {

    private final PrimeContractorService primeContractorService;

    public PrimeContractorController(PrimeContractorService primeContractorService) {
        this.primeContractorService = primeContractorService;
    }


    @GetMapping
    public ResponseEntity<List<PrimeContractor>> findAll() {
        List<PrimeContractor> primeContractors = primeContractorService.findAll();
        return ResponseEntity.ok(primeContractors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrimeContractor> findById(@PathVariable Long id) {
        PrimeContractor primeContractor = primeContractorService.findById(id);
        return ResponseEntity.ok(primeContractor);
    }

    @PostMapping
    public ResponseEntity<PrimeContractor> create(@RequestBody PrimeContractor primeContractor) {
        PrimeContractor newPrimeContractor = primeContractorService.save(primeContractor);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPrimeContractor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrimeContractor> updatePrimeContractor(@PathVariable Long id, @RequestBody PrimeContractor primeContractor) {
        PrimeContractor updatedPrimeContractor = primeContractorService.update(id, primeContractor);
        return ResponseEntity.ok(updatedPrimeContractor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrimeContractor(@PathVariable Long id) {
        primeContractorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
