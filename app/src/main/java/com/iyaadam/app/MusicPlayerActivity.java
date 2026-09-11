package com.iyaadam.app;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayerActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private List<Music> playlist;
    private int currentTrackIndex = 0;
    
    private TextView trackTitle, trackArtist, currentTime, totalTime;
    private SeekBar seekBar;
    private ImageButton playPauseBtn, nextBtn, prevBtn, closeBtn;
    private ImageView albumArt;
    private RecyclerView playlistRecycler;
    private MusicAdapter musicAdapter;
    
    private Handler handler = new Handler();
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        initViews();
        loadPlaylist();
        setupMediaPlayer();
        setupListeners();
    }

    private void initViews() {
        trackTitle = findViewById(R.id.track_title);
        trackArtist = findViewById(R.id.track_artist);
        currentTime = findViewById(R.id.current_time);
        totalTime = findViewById(R.id.total_time);
        seekBar = findViewById(R.id.seek_bar);
        playPauseBtn = findViewById(R.id.btn_play_pause);
        nextBtn = findViewById(R.id.btn_next);
        prevBtn = findViewById(R.id.btn_prev);
        closeBtn = findViewById(R.id.btn_close);
        albumArt = findViewById(R.id.album_art);
        playlistRecycler = findViewById(R.id.playlist_recycler);
        
        playlistRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadPlaylist() {
        playlist = new ArrayList<>();
        
        playlist.add(new Music("1", "Essence", "Wizkid ft. Tems", 
            "https://via.placeholder.com/300?text=Essence", 
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3", 213));
        
        playlist.add(new Music("2", "Ye", "Burna Boy", 
            "https://via.placeholder.com/300?text=Ye", 
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-2.mp3", 204));
        
        playlist.add(new Music("3", "Essence", "Tiwa Savage", 
            "https://via.placeholder.com/300?text=Tiwa", 
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-3.mp3", 237));
        
        playlist.add(new Music("4", "Ye Nwa Mi", "Fireboy DML", 
            "https://via.placeholder.com/300?text=Fireboy", 
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-4.mp3", 205));
        
        playlist.add(new Music("5", "Ye", "CKay", 
            "https://via.placeholder.com/300?text=CKay", 
            "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-5.mp3", 198));

        musicAdapter = new MusicAdapter(this, playlist, position -> {
            currentTrackIndex = position;
            playTrack();
        });
        playlistRecycler.setAdapter(musicAdapter);
    }

    private void setupMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mp -> playNext());
    }

    private void setupListeners() {
        playPauseBtn.setOnClickListener(v -> togglePlayPause());
        nextBtn.setOnClickListener(v -> playNext());
        prevBtn.setOnClickListener(v -> playPrevious());
        closeBtn.setOnClickListener(v -> finish());
        
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void playTrack() {
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            
            Music currentTrack = playlist.get(currentTrackIndex);
            trackTitle.setText(currentTrack.title);
            trackArtist.setText(currentTrack.artist);
            totalTime.setText(formatTime(currentTrack.duration));
            
            mediaPlayer.reset();
            mediaPlayer.setDataSource(currentTrack.audioUrl);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                mediaPlayer.start();
                isPlaying = true;
                playPauseBtn.setImageResource(android.R.drawable.ic_media_pause);
                updateSeekBar();
            });
            
        } catch (Exception e) {
            Toast.makeText(this, "Error playing track", Toast.LENGTH_SHORT).show();
        }
    }

    private void togglePlayPause() {
        if (isPlaying) {
            mediaPlayer.pause();
            isPlaying = false;
            playPauseBtn.setImageResource(android.R.drawable.ic_media_play);
        } else {
            mediaPlayer.start();
            isPlaying = true;
            playPauseBtn.setImageResource(android.R.drawable.ic_media_pause);
            updateSeekBar();
        }
    }

    private void playNext() {
        currentTrackIndex = (currentTrackIndex + 1) % playlist.size();
        playTrack();
    }

    private void playPrevious() {
        currentTrackIndex = (currentTrackIndex - 1 + playlist.size()) % playlist.size();
        playTrack();
    }

    private void updateSeekBar() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            seekBar.setProgress(mediaPlayer.getCurrentPosition());
            currentTime.setText(formatTime(mediaPlayer.getCurrentPosition()));
            handler.postDelayed(this::updateSeekBar, 1000);
        }
    }

    private String formatTime(int milliseconds) {
        int seconds = milliseconds / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        handler.removeCallbacksAndMessages(null);
    }
}
