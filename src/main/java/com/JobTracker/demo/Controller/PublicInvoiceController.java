package com.JobTracker.demo.Controller;

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
    private JavaMailSender mailSender;

    @PostMapping("/request-invoice")
    public ResponseEntity<?> handleInvoiceRequest(@RequestBody Map<String, String> formData){
        String customerName = formData.get("name");
        String customerEmail = formData.get("email");
        String jobDetails = formData.get("jobDetails");

        String emailBody = String.format(
                "New Invoice Requested!\n\nClient Name: %s\nClient Email: %s\nJob/Work Details: %s",
                customerName, customerEmail, jobDetails
        );

        if(mailSender != null){
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("your email");
            message.setSubject("New Invoice Requested from " + customerName);
            message.setText(emailBody);
            mailSender.send(message);
        }else{
            System.out.println("mailSender is null" + emailBody);
        }

        return ResponseEntity.ok(Map.of("message", "Invoice Requested successfully!"));
    }

}
