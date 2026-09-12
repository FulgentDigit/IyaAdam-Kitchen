package com.iyaadam.app;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CheckoutActivity extends AppCompatActivity {

    private static final String API_URL =
            "https://iyaadam.uhd.com.ng/api/create_order.php";

    private EditText nameInput;
    private EditText phoneInput;
    private EditText addressInput;
    private Button payBtn;
    private ImageView backBtn;

    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    private final Handler mainHandler =
            new Handler(Looper.getMainLooper());

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

        payBtn.setOnClickListener(v -> submitOrder());
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
            addressInput.setError("Enter delivery address");
            addressInput.requestFocus();
            return;
        }

        List<CartManager.CartItem> cartItems =
                CartManager.getInstance().getCartItems();

        if (cartItems == null || cartItems.isEmpty()) {
            Toast.makeText(
                    this,
                    "Your cart is empty",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        payBtn.setEnabled(false);
        payBtn.setText("Submitting Order...");

        executor.execute(() -> {

            try {

                JSONObject order = new JSONObject();

                order.put("customer_name", name);
                order.put("customer_phone", phone);
                order.put("delivery_address", address);
                order.put("notes", "");

                JSONArray items = new JSONArray();

                for (CartManager.CartItem cartItem : cartItems) {

                    Dish dish = cartItem.dish;

                    JSONObject item = new JSONObject();

                    item.put("dish_id", dish.id);
                    item.put("dish_name", dish.name);
                    item.put("unit_price", dish.price);
                    item.put("quantity", cartItem.quantity);

                    items.put(item);
                }

                order.put("items", items);

                String response = sendOrder(order);

                JSONObject result =
                        new JSONObject(response);

                boolean success =
                        result.optBoolean("success", false);

                String message =
                        result.optString(
                                "message",
                                "Unable to submit order."
                        );

                if (success) {

                    String orderNumber =
                            result.optString(
                                    "order_number",
                                    ""
                            );

                    mainHandler.post(() -> {

                        CartManager.getInstance().clearCart();

                        payBtn.setEnabled(true);
                        payBtn.setText("Order Submitted ✓");

                        Toast.makeText(
                                CheckoutActivity.this,
                                "Order " + orderNumber +
                                        " submitted successfully!",
                                Toast.LENGTH_LONG
                        ).show();

                        finish();
                    });

                } else {

                    mainHandler.post(() -> {

                        payBtn.setEnabled(true);
                        payBtn.setText("Place Order");

                        Toast.makeText(
                                CheckoutActivity.this,
                                message,
                                Toast.LENGTH_LONG
                        ).show();
                    });
                }

            } catch (Exception e) {

                mainHandler.post(() -> {

                    payBtn.setEnabled(true);
                    payBtn.setText("Place Order");

                    Toast.makeText(
                            CheckoutActivity.this,
                            "Network error. Please try again.",
                            Toast.LENGTH_LONG
                    ).show();
                });
            }
        });
    }

    private String sendOrder(JSONObject order) throws Exception {

        URL url = new URL(API_URL);

        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(20000);

        connection.setRequestProperty(
                "Content-Type",
                "application/json; charset=UTF-8"
        );

        connection.setRequestProperty(
                "Accept",
                "application/json"
        );

        connection.setDoOutput(true);

        byte[] data =
                order.toString().getBytes(
                        StandardCharsets.UTF_8
                );

        try (OutputStream output =
                     connection.getOutputStream()) {

            output.write(data);
            output.flush();
        }

        int responseCode =
                connection.getResponseCode();

        InputStream inputStream;

        if (responseCode >= 200 &&
                responseCode < 400) {

            inputStream =
                    connection.getInputStream();

        } else {

            inputStream =
                    connection.getErrorStream();
        }

        if (inputStream == null) {
            throw new Exception(
                    "No response from server"
            );
        }

        StringBuilder response =
                new StringBuilder();

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     inputStream,
                                     StandardCharsets.UTF_8
                             ))) {

            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        }

        connection.disconnect();

        return response.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}
