package com.JobTracker.demo.Controller;

import com.JobTracker.demo.Entity.Licence;
import com.JobTracker.demo.Service.LicenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/licences")
public class LicenceController {

    private final LicenceService licenceService;

    public LicenceController(LicenceService licenceService) {
        this.licenceService = licenceService;
    }


    @GetMapping("employee/id")
    public ResponseEntity<List<Licence>> getLicencesByEmployeeId(@RequestParam Long id) {
        List<Licence> licenceList = licenceService.findAllByEmployeeId(id);
        return ResponseEntity.ok(licenceList);
    }

    @GetMapping()
    public ResponseEntity<Licence> getLicenceById(@RequestParam Long id) {
        Licence currentLicence = licenceService.findById(id);
        return ResponseEntity.ok(currentLicence);
    }


    @PostMapping("/id")
    public ResponseEntity<Licence> createLicence(@RequestBody Licence licence, @RequestParam Long id) {
        Licence currentLicence = licenceService.createLicence(licence, id);
        return ResponseEntity.ok(currentLicence);
    }

    @DeleteMapping("/id")
    public ResponseEntity<Void> deleteLicenceById(@RequestParam Long id) {
        licenceService.deleteLicence(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/id")
    public ResponseEntity<Licence> updateLicence(@RequestBody Licence licence, @RequestParam Long id) {
        Licence currentLicence = licenceService.updateLicence(id, licence);
        return ResponseEntity.ok(currentLicence);
    }






}
