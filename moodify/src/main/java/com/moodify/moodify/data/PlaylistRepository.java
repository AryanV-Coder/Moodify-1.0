package com.moodify.moodify.data;

import java.util.*;

import org.springframework.stereotype.Component;

import com.moodify.moodify.entity.Playlist;
import com.moodify.moodify.entity.Song;
import com.moodify.moodify.enums.Mood;
import com.moodify.moodify.service.EnergeticMood;
import com.moodify.moodify.service.HappyMood;
import com.moodify.moodify.service.LoveMood;
import com.moodify.moodify.service.SadMood;

@Component
public class PlaylistRepository {
    
    private final Map<Mood, Playlist> playlists;
    
    public PlaylistRepository() {
        this.playlists = new HashMap<>();
        initializePlaylists();
    }
    
    private void initializePlaylists() {

        List<Song> happySongs = Arrays.asList(
            new Song("Dosti", new HappyMood(), "https://youtu.be/1n1rhB4XvZE?si=HkOpEjErRS-Tza1g"),
            new Song("Dil To Bachcha Hai", new HappyMood(), "https://youtu.be/_TJZaE0FkKs?si=2kR7rD1R862iRbfC"),
            new Song("Bum Bum Bole", new HappyMood(), "https://youtu.be/NJ1NIIdHhXs?si=70J6opZOwtMu6K3s"),
            new Song("Tu Jo Mila", new HappyMood(), "https://youtu.be/9i1Ri8kPoec?si=ftM_CbbGH9M_19Ao")
        );
        playlists.put(Mood.HAPPY, new Playlist("Happy Vibes", new HappyMood(), happySongs));
        

        List<Song> sadSongs = Arrays.asList(
            new Song("Phir Bhi Tumko Chaahunga", new SadMood(), "https://youtu.be/_iktURk0X-A?si=df-kjYCEzmG-A--q"),
            new Song("Kabhii Tumhhe", new SadMood(), "https://youtu.be/ByIZIKFmHOA?si=ob8vz6pCLb8H4piM"),
            new Song("Naina", new SadMood(), "https://youtu.be/KzBa4ZKTVjE?si=x0r8chQ65eO6hFBv"),
            new Song("Dil Banaane Waaleya", new SadMood(), "https://youtu.be/RKK0oNVuc1I?si=PBAdoqXq3iANaFGd")
        );
        playlists.put(Mood.SAD, new Playlist("Melancholic Moments", new SadMood(), sadSongs));
        

        List<Song> energeticSongs = Arrays.asList(
            new Song("Dance ka Bhoot", new EnergeticMood(), "https://youtu.be/xfMN4SpIxIA?si=eHLj0KqGfLpfqE0i"),
            new Song("Deva Deva", new EnergeticMood(), "https://youtu.be/WjAPDofGg28?si=UXJgAcQ82U2fmtD4"),
            new Song("Hum Hindustani", new EnergeticMood(), "https://youtu.be/d7rLZm1HmbE?si=rjp8IxKO6nNw7-Qt"),
            new Song("India Waale", new EnergeticMood(), "https://youtu.be/X-DDknSzELI?si=2nLMp5vElp33T41t")
        );
        playlists.put(Mood.ENERGETIC, new Playlist("Power Playlist", new EnergeticMood(), energeticSongs));
        

        List<Song> loveSongs = Arrays.asList(
            new Song("Aavan Javan", new LoveMood(), "https://youtu.be/enjkcCdAlXc?si=WjFfxXcFIFnJ4i72"),
            new Song("Soulmate", new LoveMood(), "https://youtu.be/k3ijQJjUbTs?si=SIsZIOxzohhmxkNM"),
            new Song("Pehla Ishq", new LoveMood(), "https://youtu.be/HDClXnzIZCw?si=P4Fta-tzvBxLPV5a"),
            new Song("Khoobsurat", new LoveMood(), "https://youtu.be/1-nnEM8chwo?si=wGyFXaLZQbuCoOsR")
        );
        playlists.put(Mood.LOVE, new Playlist("Romantic Rhythms", new LoveMood(), loveSongs));
    }

    public Song getRandomSongByMood(Mood mood) {
        Playlist playlist = playlists.get(mood);
        if (playlist == null || playlist.getSongs().isEmpty()) {
            return null;
        }
        
        List<Song> songs = playlist.getSongs();
        Random random = new Random();
        return songs.get(random.nextInt(songs.size()));
    }

}
