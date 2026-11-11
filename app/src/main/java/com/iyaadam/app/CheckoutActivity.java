package com.iyaadam.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CheckoutActivity extends AppCompatActivity {

    private static final String API_URL =
            "https://iyaadam.uhd.com.ng/api/create_order.php";

    private static final String WHATSAPP_NUMBER =
            "2347065214656";

    private static final String BANK_NAME =
            "MONIEPOINT MICROFINANCE BANK";

    private static final String ACCOUNT_NAME =
            "ABIBAT YUSUF";

    private static final String ACCOUNT_NUMBER =
            "5258217689";

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

        // Display bank transfer details.
        ((TextView) findViewById(R.id.bank_name))
                .setText(BANK_NAME);

        ((TextView) findViewById(R.id.account_name))
                .setText(ACCOUNT_NAME);

        ((TextView) findViewById(R.id.account_number))
                .setText(ACCOUNT_NUMBER);

        ((TextView) findViewById(R.id.payment_note))
                .setText("Please transfer the exact order amount "
                        + "to the account above.");

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
            Toast.makeText(this, "Your cart is empty",
                    Toast.LENGTH_SHORT).show();
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

                JSONObject result = new JSONObject(response);

                boolean success =
                        result.optBoolean("success", false);

                String message = result.optString(
                        "message",
                        "Unable to submit order."
                );

                if (success) {

                    String orderNumber =
                            result.optString("order_number", "");

                    mainHandler.post(() -> {

                        // Prepare WhatsApp message before clearing cart.
                        String whatsappMessage =
                                buildWhatsAppMessage(
                                        name,
                                        phone,
                                        address,
                                        orderNumber,
                                        cartItems
                                );

                        CartManager.getInstance().clearCart();

                        payBtn.setEnabled(true);
                        payBtn.setText("Order Submitted ✓");

                        Toast.makeText(
                                CheckoutActivity.this,
                                "Order " + orderNumber
                                        + " submitted successfully!",
                                Toast.LENGTH_LONG
                        ).show();

                        // Open WhatsApp with the complete order.
                        openWhatsApp(whatsappMessage);

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

    private String buildWhatsAppMessage(
            String name,
            String phone,
            String address,
            String orderNumber,
            List<CartManager.CartItem> cartItems
    ) {

        StringBuilder message = new StringBuilder();

        message.append("NEW ORDER - IYAADAM KITCHEN\n\n");

        if (orderNumber != null && !orderNumber.isEmpty()) {
            message.append("Order Number: ")
                    .append(orderNumber)
                    .append("\n");
        }

        message.append("\nCUSTOMER DETAILS\n");
        message.append("Name: ").append(name).append("\n");
        message.append("Phone: ").append(phone).append("\n");
        message.append("Delivery Address: ")
                .append(address).append("\n");

        message.append("\nORDERED ITEMS\n");

        double total = 0;

        for (CartManager.CartItem cartItem : cartItems) {

            Dish dish = cartItem.dish;

            double itemTotal =
                    dish.price * cartItem.quantity;

            total += itemTotal;

            message.append("\n")
                    .append(dish.name)
                    .append("\nQuantity: ")
                    .append(cartItem.quantity)
                    .append("\nUnit Price: ₦")
                    .append(formatPrice(dish.price))
                    .append("\nItem Total: ₦")
                    .append(formatPrice(itemTotal))
                    .append("\n");
        }

        message.append("\n------------------------\n");
        message.append("ORDER TOTAL: ₦")
                .append(formatPrice(total))
                .append("\n");

        message.append("\nPayment Bank: ")
                .append(BANK_NAME);

        message.append("\nAccount Name: ")
                .append(ACCOUNT_NAME);

        message.append("\nAccount Number: ")
                .append(ACCOUNT_NUMBER);

        message.append("\n\nPlease confirm this order.");

        return message.toString();
    }

    private String formatPrice(double price) {

        if (price == (long) price) {
            return String.format(
                    Locale.US,
                    "%,d",
                    (long) price
            );
        }

        return String.format(
                Locale.US,
                "%,.2f",
                price
        );
    }

    private void openWhatsApp(String message) {

        try {

            String url =
                    "https://wa.me/"
                            + WHATSAPP_NUMBER
                            + "?text="
                            + Uri.encode(message);

            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(url)
            );

            startActivity(intent);

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Order saved successfully. "
                            + "Please contact us on WhatsApp: "
                            + "0706 521 4656",
                    Toast.LENGTH_LONG
            ).show();
        }
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

        byte[] data = order.toString().getBytes(
                StandardCharsets.UTF_8
        );

        try (OutputStream output = connection.getOutputStream()) {
            output.write(data);
            output.flush();
        }

        int responseCode = connection.getResponseCode();

        InputStream inputStream;

        if (responseCode >= 200 && responseCode < 400) {
            inputStream = connection.getInputStream();
        } else {
            inputStream = connection.getErrorStream();
        }

        if (inputStream == null) {
            connection.disconnect();
            throw new Exception("No response from server");
        }

        StringBuilder response = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
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
