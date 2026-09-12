package com.iyaadam.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CheckoutActivity extends AppCompatActivity {
    private EditText nameInput, phoneInput, addressInput;
    private Button payBtn;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        backBtn = findViewById(R.id.back_btn);
        nameInput = findViewById(R.id.name_input);
        phoneInput = findViewById(R.id.phone_input);
        addressInput = findViewById(R.id.address_input);
        payBtn = findViewById(R.id.pay_btn);

        backBtn.setOnClickListener(v -> finish());
        payBtn.setOnClickListener(v -> processPayment());
    }

    private void processPayment() {
        String name = nameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Order confirmed! ✅\nDelivery to: " + address, Toast.LENGTH_LONG).show();
        CartManager.getInstance().clearCart();
        finish();
    }
}
