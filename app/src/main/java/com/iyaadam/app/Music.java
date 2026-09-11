package com.iyaadam.app;

public class Music {
    public String id;
    public String title;
    public String artist;
    public String albumArt;
    public String audioUrl;
    public int duration;

    public Music(String id, String title, String artist, String albumArt, String audioUrl, int duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.albumArt = albumArt;
        this.audioUrl = audioUrl;
        this.duration = duration;
    }
}
