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
        String graphUrl = "https://graph.microsoft.com/v1.0/users/" + targetMailbox + "/messages?$top=10&$select=subject,receivedDateTime,from,bodyPreview";

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


}

