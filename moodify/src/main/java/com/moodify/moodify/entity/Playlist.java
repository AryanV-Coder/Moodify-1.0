package com.moodify.moodify.entity;

import com.moodify.moodify.service.MoodType;

import java.util.List;

public class Playlist {

    private String name;
    private MoodType mood;
    private List<Song> songs;

    public Playlist(String name,MoodType mood,List<Song> songs){
        this.name = name;
        this.mood = mood;
        this.songs = songs;
    }

    public String getName(){
        return name;
    }
    public MoodType getMood(){
        return mood;
    }
    public List<Song> getSongs(){
        return songs;
    }
}