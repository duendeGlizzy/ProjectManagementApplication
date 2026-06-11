package com.JobTracker.demo.Service;

import com.JobTracker.demo.Entity.Vendor;
import com.JobTracker.demo.Repository.VendorRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    public List<Vendor> findAll() {
        return vendorRepository.findAll();
    }

    public Vendor findById(Long id) {
        return vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor Not Found"));
    }

    @Transactional
    public Vendor createVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }

    public Vendor update(Vendor vendor, Long id) {
        Vendor currentVendor = vendorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vendor Not Found"));

        currentVendor.setDescription(vendor.getDescription());
        currentVendor.setCompanyName(vendor.getCompanyName());
        currentVendor.setAddress(vendor.getAddress());
        currentVendor.setPhoneNumber(vendor.getPhoneNumber());

        return vendorRepository.save(currentVendor);
    }

    public void delete(Long id) {
        if(!vendorRepository.existsById(id)) {
            throw new RuntimeException("Vendor Not Found");
        }
        vendorRepository.deleteById(id);
    }




}
