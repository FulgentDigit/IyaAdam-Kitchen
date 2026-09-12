package com.iyaadam.app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.media.MediaRecorder;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

public class CustomOrderActivity extends AppCompatActivity {

    private static final int RECORD_AUDIO_REQUEST = 1001;

    private EditText requestText;
    private TextView recordingStatus;
    private Button recordButton;
    private Button submitButton;
    private ImageView backButton;

    private MediaRecorder mediaRecorder;
    private String audioFilePath;
    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_order);

        backButton = findViewById(R.id.custom_back_btn);
        requestText = findViewById(R.id.custom_request_text);
        recordingStatus = findViewById(R.id.recording_status);
        recordButton = findViewById(R.id.record_button);
        submitButton = findViewById(R.id.submit_custom_order);

        backButton.setOnClickListener(v -> finish());

        recordButton.setOnClickListener(v -> {

            if (isRecording) {
                stopRecording();
            } else {
                startRecording();
            }

        });

        submitButton.setOnClickListener(v -> submitRequest());
    }

    private void startRecording() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    RECORD_AUDIO_REQUEST
            );

            return;
        }

        File audioFile = new File(
                getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "custom_order_" + System.currentTimeMillis() + ".m4a"
        );

        audioFilePath = audioFile.getAbsolutePath();

        mediaRecorder = new MediaRecorder();

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(audioFilePath);

        try {

            mediaRecorder.prepare();
            mediaRecorder.start();

            isRecording = true;

            recordButton.setText("⏹ Stop Recording");
            recordingStatus.setText("🔴 Recording your voice request...");
            recordingStatus.setTextColor(0xFFC62828);

        } catch (IOException e) {

            Toast.makeText(
                    this,
                    "Unable to start recording",
                    Toast.LENGTH_SHORT
            ).show();

            releaseRecorder();
        }
    }

    private void stopRecording() {

        if (mediaRecorder != null) {

            try {
                mediaRecorder.stop();
            } catch (RuntimeException e) {
                // Ignore invalid short recordings
            }

            releaseRecorder();
        }

        isRecording = false;

        recordButton.setText("🎤 Record Voice Request");
        recordingStatus.setText("✓ Voice request recorded");
        recordingStatus.setTextColor(0xFF2E7D32);

        Toast.makeText(
                this,
                "Voice request recorded",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void releaseRecorder() {

        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private void submitRequest() {

        String request = requestText.getText().toString().trim();

        if (request.isEmpty() && audioFilePath == null) {

            Toast.makeText(
                    this,
                    "Please type your request or record a voice note",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        /*
         * For now we confirm the custom request locally.
         *
         * The next stage will connect this request to the Cart
         * and Checkout system so the restaurant receives:
         *
         * - Customer request
         * - Voice recording
         * - Customer details
         * - Order/payment information
         */

        Toast.makeText(
                this,
                "Custom request received! We will prepare it for checkout.",
                Toast.LENGTH_LONG
        ).show();

        finish();
    }

    @Override
    protected void onDestroy() {

        if (isRecording) {
            stopRecording();
        }

        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults
    ) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == RECORD_AUDIO_REQUEST) {

            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startRecording();

            } else {

                Toast.makeText(
                        this,
                        "Microphone permission is required for voice requests",
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }
}
