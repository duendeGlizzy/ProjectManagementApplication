package com.JobTracker.demo.Service;

import org.springframework.web.util.UriComponentsBuilder;
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

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            logger.error("OAuth token retrieval failed. Status: {}. Response: {}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new RuntimeException("MSGraph auth failed", e);

        }
    }

    private final RestClient restClient = RestClient.create();


    public Map<String, Object> getRecentEmails() {
        String accessToken = getAccessToken();
        if (accessToken == null) {
            throw new RuntimeException("MSGraph auth failed: Access token is null");
        }
        // Target endpoint for reading messages from the designated account
        URI graphUri = UriComponentsBuilder.fromUriString("https://graph.microsoft.com/v1.0/users/" + targetMailbox + "/messages")
                .queryParam("$top", "50") // Grab enough matches to sort
                .queryParam("$select", "subject,receivedDateTime,from,bodyPreview")
                .queryParam("$filter", "startswith(subject, '[INVOICE REQUEST]')")
                .build()
                .toUri();



        try {
            Map<String, Object> rawResponse = restClient.get()
                    .uri(graphUri)
                    .header("Authorization", "Bearer " + accessToken)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {});

            if (rawResponse == null || !rawResponse.containsKey("value")) {
                return rawResponse;
            }

            // 2. Extract the list of filtered emails
            List<Map<String, Object>> emails = (List<Map<String, Object>>) rawResponse.get("value");

            // 3. Sort them descending (newest first) locally in memory
            List<Map<String, Object>> sortedEmails = emails.stream()
                    .sorted((e1, e2) -> {
                        String d1 = (String) e1.get("receivedDateTime");
                        String d2 = (String) e2.get("receivedDateTime");
                        if (d1 == null || d2 == null) return 0;
                        return OffsetDateTime.parse(d2).compareTo(OffsetDateTime.parse(d1));
                    })
                    .limit(20) // Keep the top 20 newest results
                    .collect(Collectors.toList());

            // 4. Update the response payload with your beautifully sorted list
            rawResponse.put("value", sortedEmails);
            return rawResponse;

        } catch (RestClientResponseException e) {
            logger.error("MS Graph API (getRecentEmails) failed. Status: {}. Error Details: {}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
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
            logger.error("MS Graph API (sendInvoiceRequestEmail) failed. Status: {}. Error Details: {}",
                    e.getStatusCode(), e.getResponseBodyAsString(), e);
            throw new RuntimeException("MSGraph api call failed", e);
        }


    }


}

