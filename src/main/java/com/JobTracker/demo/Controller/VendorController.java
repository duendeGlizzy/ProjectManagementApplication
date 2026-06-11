package com.JobTracker.demo.Controller;

import com.JobTracker.demo.Entity.Vendor;
import com.JobTracker.demo.Service.VendorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("vendors")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @GetMapping
    public ResponseEntity<List<Vendor>> getAllVendors() {
        List<Vendor> vendors = vendorService.findAll();
        return ResponseEntity.ok(vendors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vendor> getVendorById(@PathVariable Long id) {
        Vendor vendor = vendorService.findById(id);
        return ResponseEntity.ok(vendor);
    }

    @PostMapping
    public ResponseEntity<Vendor> createVendor(@RequestBody Vendor vendor) {
        Vendor newVendor = vendorService.createVendor(vendor);
        return ResponseEntity.status(HttpStatus.CREATED).body(newVendor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vendor> updateVendor(@RequestBody Vendor vendor,
                                               @PathVariable Long id) {

        Vendor currentVendor = vendorService.update(vendor, id);
        return ResponseEntity.ok(currentVendor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendor(@PathVariable Long id) {
        vendorService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
