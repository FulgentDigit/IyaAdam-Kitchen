package com.iyaadam.app;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        View backButton = findViewById(R.id.btn_notification_back);

        backButton.setOnClickListener(v -> finish());
    }
}
