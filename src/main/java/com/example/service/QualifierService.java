package com.example.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class QualifierService {

    private final RestTemplate restTemplate = new RestTemplate();

    public void executeFlow() {
        try {
            String webhookUrl = null;
            String accessToken = null;

            String generateUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("name", "Sandeep Gour");    
            requestBody.put("regNo", "0105CS221160");    
            requestBody.put("email", "sandeepgour597@gmail.com"); 

            ResponseEntity<Map> response =
                    restTemplate.postForEntity(generateUrl, requestBody, Map.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                webhookUrl = (String) response.getBody().get("webhook");
                accessToken = (String) response.getBody().get("accessToken");

                System.out.println("Webhook URL: " + webhookUrl);
                System.out.println("Access Token: " + accessToken);
            } else {
                System.err.println("Failed to generate webhook!");
                return;
            }

        
            String finalQuery = solveSqlProblem();

            submitSolution(webhookUrl, accessToken, finalQuery);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String solveSqlProblem() {
        return "SELECT e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME, " +
                "COUNT(e2.EMP_ID) AS YOUNGER_EMPLOYEES_COUNT " +
                "FROM EMPLOYEE e1 " +
                "JOIN DEPARTMENT d ON e1.DEPARTMENT = d.DEPARTMENT_ID " +
                "LEFT JOIN EMPLOYEE e2 ON e1.DEPARTMENT = e2.DEPARTMENT " +
                "AND e2.DOB > e1.DOB " +
                "GROUP BY e1.EMP_ID, e1.FIRST_NAME, e1.LAST_NAME, d.DEPARTMENT_NAME " +
                "ORDER BY e1.EMP_ID DESC;";
    }

    private void submitSolution(String webhookUrl, String token, String finalQuery) {
        try {
            
            token = token.trim();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            headers.set("Authorization", "Bearer " + token);

            Map<String, String> body = new HashMap<>();
            body.put("finalQuery", finalQuery);

            HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

            ResponseEntity<String> response;

            try {
                response = restTemplate.postForEntity(webhookUrl, entity, String.class);
            } catch (HttpClientErrorException.Unauthorized ex) {

                System.out.println("401 Unauthorized with Bearer. Retrying without Bearer...");
                headers.set("Authorization", token);
                entity = new HttpEntity<>(body, headers);
                response = restTemplate.postForEntity(webhookUrl, entity, String.class);
            }

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Submission Successful! Response: " + response.getBody());
            } else {
                System.err.println("Submission Failed! Status: " + response.getStatusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
