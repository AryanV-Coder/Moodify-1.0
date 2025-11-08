package com.moodify.moodify.service;

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

    private static final String FASTAPI_URL = "https://moodify-fastapi-backend.onrender.com/mood-analysis";

    public String getMoodFromImage(byte[] imageBytes, String origFilename) {
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

            // Parse JSON and extract "response" field
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(resp.getBody());
            String mood = jsonNode.get("response").asText();

            return mood;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
