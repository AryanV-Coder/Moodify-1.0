package com.moodify.moodify.entity;

import com.moodify.moodify.service.MoodType;

public class Song {

    private String name;
    private MoodType mood;
    private String link;

    public Song(String name, MoodType mood, String link) {
        this.name = name;
        this.mood = mood;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public MoodType getMood() {
        return mood;
    }

    public String getLink() {
        return link;
    }
}
