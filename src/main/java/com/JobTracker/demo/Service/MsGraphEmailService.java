package com.JobTracker.demo.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class MsGraphEmailService {

    @Value("${microsoft.graph.tenantId}") private String tenantId;
    @Value("${microsoft.graph.clientId}") private String clientId;
    @Value("${microsoft.graph.clientSecret}") private String clientSecret;
    @Value("${microsoft.graph.targetMailbox}") private String targetMailbox;

    private final RestClient restClient = RestClient.create();

    private String getAccessToken() {
        String tokenUrl = "https://login.microsoftonline.com/" + tenantId + "/oauth2/token";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("scope", "https://graph.microsoft.com/.default");

        Map response = restClient.post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .body(Map.class);

        if(response != null) {

            return (String) response.get("access_token");
        }
        return null;
    }


    public Map<String, Object> getRecentEmails() {
        String accessToken = getAccessToken();
        // Target endpoint for reading messages from the designated account
        String graphUrl = "https://graph.microsoft.com/v1.0/users/" + targetMailbox + "/messages?$top=10&$select=subject,receivedDateTime,from,bodyPreview";

        return restClient.get()
                .uri(graphUrl)
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
    }


}

