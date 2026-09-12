package com.iyaadam.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CheckoutActivity extends AppCompatActivity {

    private EditText nameInput, phoneInput, addressInput;
    private TextView totalPrice, bankName, accountName, accountNumber, paymentNote;
    private Button payBtn;
    private ImageView backBtn;
    private ProgressBar progressBar;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private static final String BANK_API =
            "https://www.uhd.com.ng/iyaaadam_api/bank_details.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        backBtn = findViewById(R.id.back_btn);
        nameInput = findViewById(R.id.name_input);
        phoneInput = findViewById(R.id.phone_input);
        addressInput = findViewById(R.id.address_input);

        totalPrice = findViewById(R.id.total_price);
        bankName = findViewById(R.id.bank_name);
        accountName = findViewById(R.id.account_name);
        accountNumber = findViewById(R.id.account_number);
        paymentNote = findViewById(R.id.payment_note);

        payBtn = findViewById(R.id.pay_btn);
        progressBar = findViewById(R.id.progress_bar);

        backBtn.setOnClickListener(v -> finish());

        int total = CartManager.getInstance().getTotalPrice();
        totalPrice.setText("₦" + total);

        loadBankDetails();

        payBtn.setOnClickListener(v -> submitOrder());
    }

    private void loadBankDetails() {

        progressBar.setVisibility(View.VISIBLE);

        executor.execute(() -> {
            try {
                URL url = new URL(BANK_API);
                HttpURLConnection connection =
                        (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream())
                );

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                connection.disconnect();

                JSONObject json = new JSONObject(response.toString());

                mainHandler.post(() -> {

                    progressBar.setVisibility(View.GONE);

                    if (json.optBoolean("success")) {

                        bankName.setText(json.optString(
                                "bank_name",
                                "Bank details unavailable"
                        ));

                        accountName.setText(json.optString(
                                "account_name",
                                "Bank details unavailable"
                        ));

                        accountNumber.setText(json.optString(
                                "account_number",
                                "Bank details unavailable"
                        ));

                        paymentNote.setText(json.optString(
                                "payment_note",
                                ""
                        ));

                    } else {
                        showBankError();
                    }
                });

            } catch (Exception e) {

                mainHandler.post(() -> {
                    progressBar.setVisibility(View.GONE);
                    showBankError();
                });
            }
        });
    }

    private void showBankError() {
        bankName.setText("Unable to load bank details");
        accountName.setText("Please try again");
        accountNumber.setText("Check your internet connection");
        paymentNote.setText("");
    }

    private void submitOrder() {

        String name = nameInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim();

        if (name.isEmpty()) {
            nameInput.setError("Enter your name");
            nameInput.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            phoneInput.setError("Enter your phone number");
            phoneInput.requestFocus();
            return;
        }

        if (address.isEmpty()) {
            addressInput.setError("Enter your delivery address");
            addressInput.requestFocus();
            return;
        }

        if (CartManager.getInstance().getItemCount() == 0) {
            Toast.makeText(
                    this,
                    "Your cart is empty",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        String orderNumber =
                "IYA-" + System.currentTimeMillis();

        new android.app.AlertDialog.Builder(this)
                .setTitle("Order Ready")
                .setMessage(
                        "Order Number: " + orderNumber +
                        "\n\n" +
                        "Total: ₦" +
                        CartManager.getInstance().getTotalPrice() +
                        "\n\n" +
                        "Please transfer the exact amount to the bank account shown above."
                )
                .setPositiveButton(
                        "I've Made the Transfer",
                        (dialog, which) -> {

                            Toast.makeText(
                                    this,
                                    "Order " + orderNumber +
                                    " received successfully!",
                                    Toast.LENGTH_LONG
                            ).show();

                            CartManager.getInstance().clearCart();

                            Intent intent = new Intent(
                                    CheckoutActivity.this,
                                    MainActivity.class
                            );

                            intent.addFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                    Intent.FLAG_ACTIVITY_SINGLE_TOP
                            );

                            startActivity(intent);
                            finish();
                        }
                )
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}
