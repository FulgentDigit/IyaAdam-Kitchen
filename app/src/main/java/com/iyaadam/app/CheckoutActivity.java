package com.iyaadam.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CheckoutActivity extends AppCompatActivity {

    private EditText nameInput, phoneInput, addressInput;
    private TextView totalTV, itemsTV;
    private Button paymentBtn;
    private LocationService locationService;
    private String userLocation = "Not available";
    private int totalAmount = 0;

    private static final int LOCATION_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initViews();
        setupLocationServices();
        updatePricing();

        paymentBtn.setOnClickListener(v -> validateAndProcess());
    }

    private void initViews() {
        nameInput = findViewById(R.id.input_name);
        phoneInput = findViewById(R.id.input_phone);
        addressInput = findViewById(R.id.input_address);
        totalTV = findViewById(R.id.total_amount);
        itemsTV = findViewById(R.id.items_summary);
        paymentBtn = findViewById(R.id.btn_pay_now);
    }

    private void setupLocationServices() {
        locationService = LocationService.getInstance(this);
        
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_CODE);
        } else {
            locationService.startLocationUpdates(this);
        }
    }

    private void updatePricing() {
        CartManager cartManager = CartManager.getInstance();
        totalAmount = cartManager.getTotalPrice();
        int itemCount = cartManager.getItemCount();

        itemsTV.setText("Items: " + itemCount);
        totalTV.setText("Total: ₦" + totalAmount);
    }

    private void validateAndProcess() {
        String name = nameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (totalAmount <= 0) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current location
        Location loc = locationService.getCurrentLocation();
        if (loc != null) {
            userLocation = loc.getLatitude() + "," + loc.getLongitude();
        }

        // Process Payment with Flutterwave
        processFlutterwave(name, phone, address);
    }

    private void processFlutterwave(String name, String phone, String address) {
        String orderId = "IYAADAM-" + System.currentTimeMillis();
        
        Toast.makeText(this, "Processing payment of ₦" + totalAmount, Toast.LENGTH_LONG).show();
        
        // Simulate payment processing
        // In production, integrate Flutterwave SDK here
        
        // For now, show success after 2 seconds
        paymentBtn.postDelayed(() -> {
            CartManager.getInstance().clearCart();
            Toast.makeText(CheckoutActivity.this, "✓ Payment Successful!\nOrder ID: " + orderId, Toast.LENGTH_LONG).show();
            
            Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationService != null) {
            locationService.stopLocationUpdates();
        }
    }
}
