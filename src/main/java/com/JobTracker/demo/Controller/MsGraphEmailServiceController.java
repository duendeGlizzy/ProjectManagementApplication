package com.JobTracker.demo.Controller;

import com.JobTracker.demo.Service.MsGraphEmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emails")
public class MsGraphEmailServiceController {

    private final MsGraphEmailService msGraphEmailService;

    public MsGraphEmailServiceController(MsGraphEmailService msGraphEmailService) {
        this.msGraphEmailService = msGraphEmailService;
    }

    @GetMapping
    public ResponseEntity<?> fetchBusinessInbox(){
        return ResponseEntity.ok(msGraphEmailService.getRecentEmails());
    }

}
