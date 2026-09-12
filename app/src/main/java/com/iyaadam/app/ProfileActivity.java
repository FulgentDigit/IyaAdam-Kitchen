package com.iyaadam.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    private TextView userName, userPhone, userOrders;
    private Button logoutBtn;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        backBtn = findViewById(R.id.back_btn);
        userName = findViewById(R.id.user_name);
        userPhone = findViewById(R.id.user_phone);
        userOrders = findViewById(R.id.user_orders);
        logoutBtn = findViewById(R.id.logout_btn);

        backBtn.setOnClickListener(v -> finish());
        logoutBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            finish();
        });

        loadProfile();
    }

    private void loadProfile() {
        userName.setText("👤 Guest User");
        userPhone.setText("📞 +234 70893 64492");
        userOrders.setText("📦 Orders: 0");
    }
}
