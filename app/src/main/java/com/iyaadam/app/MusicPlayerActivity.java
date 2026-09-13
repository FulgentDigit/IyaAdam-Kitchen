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

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayerActivity extends AppCompatActivity {

private static final String MUSIC_API =
        "https://iyaadam.uhd.com.ng/api/music.php";

private MediaPlayer mediaPlayer;
private List<Music> playlist = new ArrayList<>();
private int currentTrackIndex = 0;

private TextView trackTitle, trackArtist, currentTime, totalTime;
private SeekBar seekBar;
private ImageButton playPauseBtn, nextBtn, prevBtn, closeBtn;
private ImageView albumArt;
private RecyclerView playlistRecycler;
private MusicAdapter musicAdapter;

private final Handler handler = new Handler();
private boolean isPlaying = false;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_music_player);

    initViews();
    setupMediaPlayer();
    setupListeners();
    loadPlaylistFromServer();
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

    playlistRecycler.setLayoutManager(
            new LinearLayoutManager(this)
    );

    playPauseBtn.setImageResource(
            android.R.drawable.ic_media_play
    );
}

private void setupMediaPlayer() {
    mediaPlayer = new MediaPlayer();

    mediaPlayer.setOnCompletionListener(mp -> {
        isPlaying = false;
        playNext();
    });
}

private void setupListeners() {

    playPauseBtn.setOnClickListener(v ->
            togglePlayPause());

    nextBtn.setOnClickListener(v ->
            playNext());

    prevBtn.setOnClickListener(v ->
            playPrevious());

    closeBtn.setOnClickListener(v ->
            finish());

    seekBar.setOnSeekBarChangeListener(
            new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(
                        SeekBar seekBar,
                        int progress,
                        boolean fromUser) {

                    if (fromUser &&
                            mediaPlayer != null &&
                            mediaPlayer.isPlaying()) {

                        mediaPlayer.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(
                        SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(
                        SeekBar seekBar) {
                }
            }
    );
}

private void loadPlaylistFromServer() {

    new Thread(() -> {

        HttpURLConnection connection = null;

        try {

            URL url = new URL(MUSIC_API);

            connection =
                    (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);

            int responseCode =
                    connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new Exception(
                        "Server returned " + responseCode
                );
            }

            InputStream inputStream =
                    connection.getInputStream();

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(inputStream)
                    );

            StringBuilder result =
                    new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            reader.close();

            JSONObject response =
                    new JSONObject(result.toString());

            if (!response.optBoolean("success", false)) {
                throw new Exception(
                        response.optString(
                                "message",
                                "Unable to load music"
                        )
                );
            }

            JSONArray items =
                    response.optJSONArray("items");

            if (items == null) {
                throw new Exception("No music found");
            }

            List<Music> serverPlaylist =
                    new ArrayList<>();

            for (int i = 0; i < items.length(); i++) {

                JSONObject item =
                        items.getJSONObject(i);

                String id =
                        item.optString("id", "");

                String title =
                        item.optString("title", "Untitled");

                String artist =
                        item.optString("artist", "IyaAdam Kitchen");

                String albumArt =
                        item.optString(
                                "album_art_url",
                                ""
                        );

                String audioUrl =
                        item.optString(
                                "audio_url",
                                ""
                        );

                int duration =
                        item.optInt("duration", 0);

                if (!audioUrl.isEmpty()) {

                    serverPlaylist.add(
                            new Music(
                                    id,
                                    title,
                                    artist,
                                    albumArt,
                                    audioUrl,
                                    duration
                            )
                    );
                }
            }

            runOnUiThread(() -> {

                playlist.clear();
                playlist.addAll(serverPlaylist);

                musicAdapter =
                        new MusicAdapter(
                                MusicPlayerActivity.this,
                                playlist,
                                position -> {

                                    if (position >= 0 &&
                                            position < playlist.size()) {

                                        currentTrackIndex =
                                                position;

                                        playTrack();
                                    }
                                }
                        );

                playlistRecycler.setAdapter(
                        musicAdapter
                );

                if (playlist.isEmpty()) {

                    trackTitle.setText(
                            "No music available"
                    );

                    trackArtist.setText(
                            "Check back later"
                    );

                    Toast.makeText(
                            MusicPlayerActivity.this,
                            "No music available yet",
                            Toast.LENGTH_SHORT
                    ).show();

                } else {

                    currentTrackIndex = 0;

                    showTrackInfo(
                            playlist.get(0)
                    );
                }
            });

        } catch (Exception e) {

            runOnUiThread(() -> {

                Toast.makeText(
                        MusicPlayerActivity.this,
                        "Unable to load music",
                        Toast.LENGTH_SHORT
                ).show();

                trackTitle.setText(
                        "Music unavailable"
                );

                trackArtist.setText(
                        "Please check your internet connection"
                );
            });

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }

    }).start();
}

private void showTrackInfo(Music track) {

    trackTitle.setText(track.title);
    trackArtist.setText(track.artist);

    totalTime.setText(
            formatTime(track.duration * 1000)
    );

    currentTime.setText("00:00");

    seekBar.setProgress(0);

    seekBar.setMax(
            track.duration > 0
                    ? track.duration * 1000
                    : 0
    );

    if (track.albumArt != null &&
            !track.albumArt.isEmpty()) {

        Glide.with(this)
                .load(track.albumArt)
                .placeholder(
                        android.R.drawable.ic_menu_gallery
                )
                .error(
                        android.R.drawable.ic_menu_gallery
                )
                .into(albumArt);

    } else {

        albumArt.setImageResource(
                android.R.drawable.ic_menu_gallery
        );
    }
}

private void playTrack() {

    if (playlist.isEmpty()) {
        return;
    }

    try {

        Music currentTrack =
                playlist.get(currentTrackIndex);

        showTrackInfo(currentTrack);

        isPlaying = false;

        mediaPlayer.reset();

        mediaPlayer.setDataSource(
                currentTrack.audioUrl
        );

        mediaPlayer.setOnPreparedListener(mp -> {

            seekBar.setMax(
                    mediaPlayer.getDuration()
            );

            totalTime.setText(
                    formatTime(
                            mediaPlayer.getDuration()
                    )
            );

            mediaPlayer.start();

            isPlaying = true;

            playPauseBtn.setImageResource(
                    android.R.drawable.ic_media_pause
            );

            updateSeekBar();
        });

        mediaPlayer.setOnErrorListener(
                (mp, what, extra) -> {

                    isPlaying = false;

                    playPauseBtn.setImageResource(
                            android.R.drawable.ic_media_play
                    );

                    Toast.makeText(
                            MusicPlayerActivity.this,
                            "Unable to play this track",
                            Toast.LENGTH_SHORT
                    ).show();

                    return true;
                }
        );

        mediaPlayer.prepareAsync();

    } catch (Exception e) {

        isPlaying = false;

        Toast.makeText(
                this,
                "Error playing track",
                Toast.LENGTH_SHORT
        ).show();
    }
}

private void togglePlayPause() {

    if (playlist.isEmpty()) {
        return;
    }

    try {

        if (mediaPlayer.isPlaying()) {

            mediaPlayer.pause();

            isPlaying = false;

            playPauseBtn.setImageResource(
                    android.R.drawable.ic_media_play
            );

        } else {

            if (mediaPlayer.getDuration() > 0) {

                mediaPlayer.start();

                isPlaying = true;

                playPauseBtn.setImageResource(
                        android.R.drawable.ic_media_pause
                );

                updateSeekBar();

            } else {

                playTrack();
            }
        }

    } catch (Exception e) {
        playTrack();
    }
}

private void playNext() {

    if (playlist.isEmpty()) {
        return;
    }

    currentTrackIndex =
            (currentTrackIndex + 1)
                    % playlist.size();

    playTrack();
}

private void playPrevious() {

    if (playlist.isEmpty()) {
        return;
    }

    currentTrackIndex =
            (currentTrackIndex - 1 +
                    playlist.size())
                    % playlist.size();

    playTrack();
}

private void updateSeekBar() {

    if (mediaPlayer != null &&
            isPlaying &&
            mediaPlayer.isPlaying()) {

        int position =
                mediaPlayer.getCurrentPosition();

        seekBar.setProgress(position);

        currentTime.setText(
                formatTime(position)
        );

        handler.postDelayed(
                this::updateSeekBar,
                500
        );
    }
}

private String formatTime(int milliseconds) {

    int seconds =
            milliseconds / 1000;

    int minutes =
            seconds / 60;

    seconds =
            seconds % 60;

    return String.format(
            "%02d:%02d",
            minutes,
            seconds
    );
}

@Override
protected void onDestroy() {

    handler.removeCallbacksAndMessages(null);

    if (mediaPlayer != null) {

        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }

        mediaPlayer.release();
        mediaPlayer = null;
    }

    super.onDestroy();
}

}
