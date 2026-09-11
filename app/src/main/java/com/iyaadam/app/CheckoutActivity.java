package com.iyaadam.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Button orderButton = findViewById(R.id.btn_place_order);
        orderButton.setOnClickListener(v -> {
            Toast.makeText(CheckoutActivity.this, "Order Placed Successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
