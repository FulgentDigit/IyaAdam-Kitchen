package com.iyaadam.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView ordersRecycler;
    private ProgressBar progressBar;
    private TextView emptyMessage;

    private OrderHistoryAdapter adapter;
    private final List<OrderHistoryAdapter.Order> orders = new ArrayList<>();

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        preferences = getSharedPreferences("iyaadam_profile", Context.MODE_PRIVATE);

        ordersRecycler = findViewById(R.id.orders_recycler);
        progressBar = findViewById(R.id.progress_bar);
        emptyMessage = findViewById(R.id.empty_message);

        findViewById(R.id.back_btn).setOnClickListener(v -> finish());

        ordersRecycler.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter = new OrderHistoryAdapter(
                this,
                orders,
                order -> {
                    Intent intent = new Intent(
                            OrderHistoryActivity.this,
                            ReceiptActivity.class
                    );

                    intent.putExtra("order_json", order.rawJson);

                    startActivity(intent);
                }
        );

        ordersRecycler.setAdapter(adapter);

        loadOrders();
    }

    private void loadOrders() {

        String phone = preferences.getString("phone", "").trim();

        if (phone.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            emptyMessage.setVisibility(View.VISIBLE);
            emptyMessage.setText(
                    "Please save your phone number in your profile to view your orders."
            );
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        emptyMessage.setVisibility(View.GONE);

        executor.execute(() -> {

            HttpURLConnection connection = null;

            try {

                String encodedPhone =
                        URLEncoder.encode(phone, "UTF-8");

                URL url = new URL(
                        "https://iyaadam.uhd.com.ng/api/order_history.php?phone="
                                + encodedPhone
                );

                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);

                int responseCode = connection.getResponseCode();

                InputStream inputStream;

                if (responseCode >= 200 && responseCode < 300) {
                    inputStream = connection.getInputStream();
                } else {
                    inputStream = connection.getErrorStream();
                }

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(inputStream)
                );

                StringBuilder response = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                JSONObject json = new JSONObject(
                        response.toString()
                );

                if (!json.optBoolean("success", false)) {
                    throw new Exception(
                            json.optString(
                                    "message",
                                    "Unable to load orders."
                            )
                    );
                }

                JSONArray array =
                        json.optJSONArray("orders");

                orders.clear();

                if (array != null) {

                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object =
                                array.getJSONObject(i);

                        OrderHistoryAdapter.Order order =
                                new OrderHistoryAdapter.Order();

                        order.id =
                                object.optLong("id");

                        order.orderNumber =
                                object.optString("order_number");

                        order.customerName =
                                object.optString("customer_name");

                        order.totalAmount =
                                object.optDouble("total_amount");

                        order.status =
                                object.optString("status");

                        order.createdAt =
                                object.optString("created_at");

                        order.rawJson =
                                object.toString();

                        orders.add(order);
                    }
                }

                handler.post(() -> {

                    progressBar.setVisibility(View.GONE);

                    adapter.notifyDataSetChanged();

                    if (orders.isEmpty()) {

                        emptyMessage.setVisibility(View.VISIBLE);
                        emptyMessage.setText(
                                "No transactions found yet."
                        );

                    } else {

                        emptyMessage.setVisibility(View.GONE);
                    }
                });

            } catch (Exception e) {

                handler.post(() -> {

                    progressBar.setVisibility(View.GONE);

                    emptyMessage.setVisibility(View.VISIBLE);

                    emptyMessage.setText(
                            "Unable to load your transactions."
                    );

                    Toast.makeText(
                            OrderHistoryActivity.this,
                            e.getMessage() != null
                                    ? e.getMessage()
                                    : "Connection error",
                            Toast.LENGTH_SHORT
                    ).show();
                });

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        executor.shutdownNow();
    }
}
