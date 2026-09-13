package com.iyaadam.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView menuRecycler;

    private final List<Dish> allDishes = new ArrayList<>();
    private final List<Dish> displayedDishes = new ArrayList<>();

    private DishAdapter adapter;

    private final ExecutorService executor =
            Executors.newSingleThreadExecutor();

    private static final String MENU_API =
            "https://iyaadam.uhd.com.ng/api/menu.php";

    private static final String WHATSAPP_NUMBER =
            "2347089364492";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        initializeViews();
        setupRecyclerView();
        setupButtons();

        loadLocalMenu();
        loadMenuFromServer();
    }

    private void initializeViews() {

        menuRecycler =
                findViewById(R.id.menu_recycler);
    }

    private void setupRecyclerView() {

        menuRecycler.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter = new DishAdapter(
                this,
                displayedDishes,
                new DishAdapter.OnDishClickListener() {

                    @Override
                    public void onDishClick(Dish dish) {

                        // Open dish details if/when available.
                        Toast.makeText(
                                MenuActivity.this,
                                dish.name,
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                    @Override
                    public void onAddToCart(Dish dish) {

                        // CORRECT CartManager method.
                        CartManager
                                .getInstance()
                                .addToCart(dish);

                        Toast.makeText(
                                MenuActivity.this,
                                dish.name + " added to cart",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );

        menuRecycler.setAdapter(adapter);
    }

    private void setupButtons() {

        ImageView backBtn =
                findViewById(R.id.back_btn);

        if (backBtn != null) {

            backBtn.setOnClickListener(v ->
                    finish()
            );
        }

        Button all =
                findViewById(R.id.category_all);

        Button african =
                findViewById(R.id.category_african);

        Button continental =
                findViewById(R.id.category_continental);

        Button grills =
                findViewById(R.id.category_grills);

        Button rice =
                findViewById(R.id.category_rice);

        Button soups =
                findViewById(R.id.category_soups);

        if (all != null) {
            all.setOnClickListener(v ->
                    showCategory("All")
            );
        }

        if (african != null) {
            african.setOnClickListener(v ->
                    showCategory("African")
            );
        }

        if (continental != null) {
            continental.setOnClickListener(v ->
                    showCategory("Continental")
            );
        }

        if (grills != null) {
            grills.setOnClickListener(v ->
                    showCategory("Grills")
            );
        }

        if (rice != null) {
            rice.setOnClickListener(v ->
                    showCategory("Rice")
            );
        }

        if (soups != null) {
            soups.setOnClickListener(v ->
                    showCategory("Soups")
            );
        }

        View customOrder =
                findViewById(R.id.custom_order_btn);

        if (customOrder != null) {

            customOrder.setOnClickListener(v -> {

                Intent intent =
                        new Intent(
                                MenuActivity.this,
                                CustomOrderActivity.class
                        );

                startActivity(intent);
            });
        }

        TextView whatsapp =
                findViewById(R.id.whatsapp_btn);

        if (whatsapp != null) {

            whatsapp.setOnClickListener(v ->
                    openWhatsApp()
            );
        }
    }

    /**
     * Temporary local menu.
     * These dishes are displayed immediately while
     * the live menu is loading from the server.
     */
    private void loadLocalMenu() {

        allDishes.clear();

        allDishes.add(
                new Dish(
                        "fufu_ewedu",
                        "Fufu & Ewedu",
                        2500,
                        "Freshly prepared fufu served with delicious ewedu soup.",
                        "images",
                        4.8,
                        125,
                        25,
                        true
                )
        );

        allDishes.add(
                new Dish(
                        "pounded_yam",
                        "Pounded Yam",
                        2500,
                        "Smooth traditional pounded yam served with Nigerian soup.",
                        "images2",
                        4.7,
                        98,
                        25,
                        true
                )
        );

        allDishes.add(
                new Dish(
                        "jollof_chicken",
                        "Jollof Rice & Chicken",
                        3000,
                        "Delicious Nigerian party-style jollof rice with chicken.",
                        "images3",
                        4.9,
                        210,
                        30,
                        true
                )
        );

        allDishes.add(
                new Dish(
                        "fried_rice",
                        "Fried Rice & Chicken",
                        3000,
                        "Special fried rice served with tasty chicken.",
                        "images4",
                        4.7,
                        145,
                        30,
                        true
                )
        );

        allDishes.add(
                new Dish(
                        "white_rice",
                        "White Rice & Stew",
                        2500,
                        "Steamed white rice served with rich Nigerian stew.",
                        "images5",
                        4.6,
                        87,
                        25,
                        true
                )
        );

        allDishes.add(
                new Dish(
                        "pepper_soup",
                        "Pepper Soup",
                        2500,
                        "Hot and spicy Nigerian pepper soup.",
                        "images6",
                        4.8,
                        76,
                        20,
                        true
                )
        );

        allDishes.add(
                new Dish(
                        "grilled_chicken",
                        "Grilled Chicken",
                        3500,
                        "Well-seasoned and perfectly grilled chicken.",
                        "images7",
                        4.9,
                        112,
                        35,
                        true
                )
        );

        allDishes.add(
                new Dish(
                        "beef_suya",
                        "Beef Suya",
                        2500,
                        "Spicy Nigerian-style grilled beef suya.",
                        "images8",
                        4.8,
                        134,
                        20,
                        true
                )
        );

        displayedDishes.clear();
        displayedDishes.addAll(allDishes);

        adapter.notifyDataSetChanged();
    }

    /**
     * Loads the current menu from the live backend.
     */
    private void loadMenuFromServer() {

        executor.execute(() -> {

            HttpURLConnection connection = null;

            try {

                URL url =
                        new URL(MENU_API);

                connection =
                        (HttpURLConnection)
                                url.openConnection();

                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setDoInput(true);

                int responseCode =
                        connection.getResponseCode();

                if (
                        responseCode !=
                                HttpURLConnection.HTTP_OK
                ) {

                    return;
                }

                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        connection.getInputStream()
                                )
                        );

                StringBuilder response =
                        new StringBuilder();

                String line;

                while (
                        (line = reader.readLine())
                                != null
                ) {

                    response.append(line);
                }

                reader.close();

                JSONObject root =
                        new JSONObject(
                                response.toString()
                        );

                boolean success =
                        root.optBoolean(
                                "success",
                                false
                        );

                if (!success) {
                    return;
                }

                JSONArray items =
                        root.optJSONArray("items");

                if (
                        items == null ||
                        items.length() == 0
                ) {
                    return;
                }

                List<Dish> serverDishes =
                        new ArrayList<>();

                for (
                        int i = 0;
                        i < items.length();
                        i++
                ) {

                    JSONObject item =
                            items.getJSONObject(i);

                    String dishId =
                            item.optString(
                                    "dish_id",
                                    item.optString(
                                            "id",
                                            String.valueOf(i)
                                    )
                            );

                    String name =
                            item.optString(
                                    "name",
                                    "Dish"
                            );

                    String description =
                            item.optString(
                                    "description",
                                    ""
                            );

                    double priceValue =
                            item.optDouble(
                                    "price",
                                    0
                            );

                    int price =
                            (int) priceValue;

                    String imageUrl =
                            item.optString(
                                    "image_url",
                                    ""
                            );

                    double rating =
                            item.optDouble(
                                    "rating",
                                    4.8
                            );

                    int reviewCount =
                            item.optInt(
                                    "review_count",
                                    0
                            );

                    int prepTime =
                            item.optInt(
                                    "prep_time",
                                    30
                            );

                    boolean available =
                            item.optInt(
                                    "available",
                                    1
                            ) == 1;

                    if (!available) {
                        continue;
                    }

                    serverDishes.add(
                            new Dish(
                                    dishId,
                                    name,
                                    price,
                                    description,
                                    imageUrl,
                                    rating,
                                    reviewCount,
                                    prepTime,
                                    true
                            )
                    );
                }

                runOnUiThread(() -> {

                    if (!serverDishes.isEmpty()) {

                        allDishes.clear();

                        allDishes.addAll(
                                serverDishes
                        );

                        displayedDishes.clear();

                        displayedDishes.addAll(
                                serverDishes
                        );

                        adapter.notifyDataSetChanged();
                    }
                });

            } catch (Exception ignored) {

                // Keep the local menu if
                // the server cannot be reached.

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }

    private void showCategory(String category) {

        displayedDishes.clear();

        if (
                category == null ||
                category.equalsIgnoreCase("All")
        ) {

            displayedDishes.addAll(
                    allDishes
            );

        } else {

            for (Dish dish : allDishes) {

                if (matchesCategory(
                        dish,
                        category
                )) {

                    displayedDishes.add(dish);
                }
            }
        }

        adapter.notifyDataSetChanged();

        if (displayedDishes.isEmpty()) {

            Toast.makeText(
                    this,
                    "No dishes found in this category",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private boolean matchesCategory(
            Dish dish,
            String category
    ) {

        String name =
                dish.name == null
                        ? ""
                        : dish.name.toLowerCase();

        String description =
                dish.description == null
                        ? ""
                        : dish.description.toLowerCase();

        String search =
                category.toLowerCase();

        if (search.equals("african")) {

            return true;
        }

        if (search.equals("rice")) {

            return name.contains("rice") ||
                    description.contains("rice");
        }

        if (search.equals("soups")) {

            return name.contains("soup") ||
                    description.contains("soup") ||
                    name.contains("ewedu");
        }

        if (search.equals("grills")) {

            return name.contains("grill") ||
                    name.contains("suya") ||
                    name.contains("chicken");
        }

        if (search.equals("continental")) {

            return !name.contains("fufu") &&
                    !name.contains("ewedu") &&
                    !name.contains("pounded") &&
                    !name.contains("suya") &&
                    !name.contains("jollof") &&
                    !name.contains("fried rice");
        }

        return true;
    }

    private void openWhatsApp() {

        String message =
                "Hello IyaAdam Kitchen, I would like to make an order.";

        try {

            String url =
                    "https://wa.me/" +
                            WHATSAPP_NUMBER +
                            "?text=" +
                            Uri.encode(message);

            Intent intent =
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(url)
                    );

            startActivity(intent);

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "WhatsApp is not available",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        executor.shutdownNow();
    }
}
