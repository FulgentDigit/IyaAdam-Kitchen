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
import com.flutterwave.raveandroid.RavePayManager;
import com.flutterwave.raveandroid.RavePayManager.Builder;
import com.flutterwave.raveandroid.interfaces.RavePayStatusListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity implements RavePayStatusListener {

    private EditText nameInput, phoneInput, addressInput;
    private TextView totalTV, itemsTV;
    private Button paymentBtn;
    private LocationService locationService;
    private String userLocation = "Not available";
    private int totalAmount = 0;

    private static final int LOCATION_PERMISSION_CODE = 100;
    private static final String FLW_PUBLIC_KEY = "FLWPUBK-d8ed87f9cb0b2016d176711e2bd0b3c2-X";

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

        // Initiate Flutterwave Payment
        initiateFlutterwave(name, phone, address);
    }

    private void initiateFlutterwave(String name, String phone, String address) {
        String orderId = "IYAADAM-" + System.currentTimeMillis();
        
        RavePayManager ravePayManager = new Builder()
                .setAmount(totalAmount)
                .setPublicKey(FLW_PUBLIC_KEY)
                .setEmail(phone + "@iyaadam.com")
                .setCurrency("NGN")
                .setfullName(name)
                .setPhoneNumber(phone)
                .setNarration("IyaAdam Kitchen Order")
                .setMetaData(new String[]{"Order ID", orderId, "Address", address, "Location", userLocation})
                .setTxRef(orderId)
                .acceptAccountPayments()
                .acceptCardPayments()
                .acceptUSSDPayments()
                .acceptBankTransfers()
                .showStagingLabel(false)
                .onStagingKeyAdded()
                .setRavePayStatusListener(this)
                .build();

        ravePayManager.launchRavePayUI();
    }

    @Override
    public void onSuccessful(com.flutterwave.raveandroid.responses.RavePayResponse response) {
        if (response.isSuccessful()) {
            // Payment successful
            CartManager.getInstance().clearCart();
            
            Toast.makeText(this, "Payment Successful! Order placed.", Toast.LENGTH_LONG).show();
            
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onError(String errorMessage) {
        Toast.makeText(this, "Payment Error: " + errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestValidationError(String message) {
        Toast.makeText(this, "Validation Error: " + message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestFailure(Exception e) {
        Toast.makeText(this, "Request Failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationService.stopLocationUpdates();
    }
}
