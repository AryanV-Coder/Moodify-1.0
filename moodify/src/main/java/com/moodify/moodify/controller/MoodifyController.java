package com.moodify.moodify.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.moodify.moodify.data.PlaylistRepository;
import com.moodify.moodify.entity.Song;
import com.moodify.moodify.enums.Mood;
import com.moodify.moodify.service.AIServiceClient;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class MoodifyController {

    @Autowired
    private AIServiceClient aiServiceClient;

    @Autowired
    private PlaylistRepository playlistRepository;

    @PostMapping("/analyse")
    public ResponseEntity<Map<String, Object>> analyse(@RequestParam("image") MultipartFile image) {
        try {
            byte[] imageBytes = image.getBytes();
            String fileName = image.getOriginalFilename();
            System.out.println("✅ Image received successfully, size : " + imageBytes.length + " bytes, " + "filename : " + fileName);


            String moodString = aiServiceClient.getMoodFromImage(imageBytes, fileName);
            System.out.println("MOOD : " + moodString);

            // Converting string to Mood enum
            Mood mood = Mood.valueOf(moodString.toUpperCase());

            Song randomSong = playlistRepository.getRandomSongByMood(mood);

            // Preparing response in JSON format
            Map<String, Object> response = new HashMap<>();
            
            if (randomSong != null) {
                response.put("song_name", randomSong.getName());
                response.put("song_mood", moodString);
                response.put("song_link", randomSong.getLink());
                response.put("message", randomSong.getMood().getMessage());
                System.out.println("🎵 Random song: " + randomSong.getName());
            } else {
                response.put("song_name", "No song available");
                response.put("song_mood", moodString);
                response.put("song_link", "");
                response.put("message", "No song found for this mood");
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
