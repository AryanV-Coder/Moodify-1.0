package com.moodify.moodify.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AIServiceClient {

    private static final String FASTAPI_URL = "http://127.0.0.1:8000/mood-analysis";

    public Map<String, String> getMoodFromImage(byte[] imageBytes, String origFilename) {
        Map<String, String> result = new HashMap<>();
        
        try {
            RestTemplate rest = new RestTemplate();

            ByteArrayResource bar = new ByteArrayResource(imageBytes) {
                @Override
                public String getFilename() {
                    return origFilename == null ? "upload.jpg" : origFilename;
                }
            };

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", bar);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> resp = rest.postForEntity(FASTAPI_URL, requestEntity, String.class);

            // Parse JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(resp.getBody());
            
            // Extract mood and comment from the new response format
            String mood = jsonNode.has("mood") ? jsonNode.get("mood").asText() : "UNKNOWN";
            String comment = jsonNode.has("comment") ? jsonNode.get("comment").asText() : "No comment available";
            
            result.put("mood", mood);
            result.put("comment", comment);
            
            System.out.println("✅ Mood detected: " + mood);
            System.out.println("✅ AI Comment: " + comment);

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            result.put("mood", "ERROR");
            result.put("comment", "Error: " + e.getMessage());
            return result;
        }
    }
}
