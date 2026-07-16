package com.JobTracker.demo.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Service
public class MsGraphEmailService {

    private static final Logger logger = LoggerFactory.getLogger(MsGraphEmailService.class);

    @Value("${microsoft.graph.tenantId}") private String tenantId;
    @Value("${microsoft.graph.clientId}") private String clientId;
    @Value("${microsoft.graph.clientSecret}") private String clientSecret;
    @Value("${microsoft.graph.targetMailbox}") private String targetMailbox;

    private String getAccessToken() {
        String tokenUrl = "https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("scope", "https://graph.microsoft.com/.default");

        try {


            Map response = restClient.post()
                    .uri(tokenUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(body)
                    .retrieve()
                    .body(Map.class);

            if (response != null && response.containsKey("access_token")) {
                return (String) response.get("access_token");
            }

            logger.warn("OAuth token response did not contain access token.");
            return null;

        }catch (RestClientResponseException e) {
            logger.error("failed to retrieve access token", e);
            throw new RuntimeException("MSGraph auth failed", e);

        }
    }

    private final RestClient restClient = RestClient.create();


    public Map<String, Object> getRecentEmails() {
        String accessToken = getAccessToken();
        // Target endpoint for reading messages from the designated account
        String graphUrl = "https://graph.microsoft.com/v1.0/users/" + targetMailbox +
                "/messages?$top=20" +
                "&$select=subject,receivedDateTime,from,bodyPreview" +
                "&$filter=startsWith(subject, '[INVOICE REQUEST]')" +
                "&$orderBy=receivedDateTime desc";
        try {


            return restClient.get()
                    .uri(graphUrl)
                    .header("Authorization", "Bearer " + accessToken)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {
                    });
        }catch (RestClientResponseException e) {
            logger.error("failed to retrieve recent emails.", e);
            throw new RuntimeException("MSGraph api call failed", e);
        }
    }


    public void sendInvoiceRequestEmail(String customerName, String customerEmail, String details){
        String accessToken = getAccessToken();
        if(accessToken == null){
            throw new RuntimeException("MSGraph auth failed");
        }

        String sendUrl = "https://graph.microsoft.com/v1.0/users/" + targetMailbox + "/sendMail";

        Map<String, Object> emailPayload = Map.of(
                "message", Map.of(
                        "subject", "[INVOICE REQUEST] from " + customerName,
                        "body", Map.of(
                                "contentType", "HTML",
                                "content", String.format(
                                        "<h3>New Invoice Requested!</h3>" +
                                                "<p><strong>Client Name:</strong> %s</p>" +
                                                "<p><strong>Client Email:</strong> %s</p>" +
                                                "<p><strong>Job/Work Details:</strong></p>" +
                                                "<p>%s</p>" +
                                                "<hr/>" +
                                                "<p><em>Sent automatically via Job Tracker Client Module.</em></p>",
                                        customerName, customerEmail, details
                                )
                        ),
                        "toRecipients", List.of(
                                Map.of("emailAddress", Map.of("address", targetMailbox)) // Sends the email straight to your own inbox
                        )
                ),
                "saveToSentItems", "false" // Keeps your Sent folder clean of auto-generated requests
        );


        try{
            restClient.post()
                    .uri(sendUrl)
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(emailPayload)
                    .retrieve()
                    .toBodilessEntity();

            logger.info("Email sent successfully");
        }catch (RestClientResponseException e){
            logger.error("failed to send email", e);
            throw new RuntimeException("MSGraph api call failed", e);
        }


    }


}

