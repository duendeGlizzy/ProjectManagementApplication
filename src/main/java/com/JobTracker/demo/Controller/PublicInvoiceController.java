package com.JobTracker.demo.Controller;

import com.JobTracker.demo.Service.MsGraphEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/public")
public class PublicInvoiceController {

    @Autowired(required = false)
    private MsGraphEmailService msGraphEmailService;

    @PostMapping("/request-invoice")
    public ResponseEntity<?> handleInvoiceRequest(@RequestBody Map<String, String> formData){
        String customerName = formData.get("name");
        String customerEmail = formData.get("email");
        String jobDetails = formData.get("jobDetails");

        try{
            msGraphEmailService.sendInvoiceRequestEmail(customerName, customerEmail, jobDetails);
            return ResponseEntity.ok(Map.of("message", "Invoice Requested successfully!"));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(Map.of("error", "could not send invoice request! please try again later"));
        }

    }

}
