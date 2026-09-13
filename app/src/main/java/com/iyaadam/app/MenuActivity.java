package com.iyaadam.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView menuRecycler;
    private DishAdapter adapter;

    private final List<Dish> allDishes = new ArrayList<>();
    private final List<Dish> displayedDishes = new ArrayList<>();

    private ExecutorService executorService;

    private static final String MENU_API =
            "https://iyaadam.uhd.com.ng/api/menu.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_menu);

        executorService = Executors.newSingleThreadExecutor();

        menuRecycler = findViewById(R.id.menu_recycler);

        menuRecycler.setLayoutManager(
                new LinearLayoutManager(this)
        );

        adapter = new DishAdapter(
                this,
                displayedDishes,
                new DishAdapter.OnDishClickListener() {

                    @Override
                    public void onDishClick(Dish dish) {
                        addDishToCart(dish);
                    }

                    @Override
                    public void onAddToCart(Dish dish) {
                        addDishToCart(dish);
                    }
                }
        );

        menuRecycler.setAdapter(adapter);

        setupButtons();

        // Load local dishes immediately.
        loadLocalMenu();

        // Then replace them with the live website menu.
        loadLiveMenu();
    }

    private void setupButtons() {

        ImageView backButton =
                findViewById(R.id.back_btn);

        if (backButton != null) {
            backButton.setOnClickListener(v -> finish());
        }

        TextView allButton =
                findViewById(R.id.category_all);

        TextView africanButton =
                findViewById(R.id.category_african);

        TextView continentalButton =
                findViewById(R.id.category_continental);

        TextView grillsButton =
                findViewById(R.id.category_grills);

        TextView riceButton =
                findViewById(R.id.category_rice);

        TextView soupsButton =
                findViewById(R.id.category_soups);

        if (allButton != null) {
            allButton.setOnClickListener(v ->
                    showCategory("all")
            );
        }

        if (africanButton != null) {
            africanButton.setOnClickListener(v ->
                    showCategory("African")
            );
        }

        if (continentalButton != null) {
            continentalButton.setOnClickListener(v ->
                    showCategory("Continental")
            );
        }

        if (grillsButton != null) {
            grillsButton.setOnClickListener(v ->
                    showCategory("Grills")
            );
        }

        if (riceButton != null) {
            riceButton.setOnClickListener(v ->
                    showCategory("Rice")
            );
        }

        if (soupsButton != null) {
            soupsButton.setOnClickListener(v ->
                    showCategory("Soups")
            );
        }

        // Custom Order is a LinearLayout in your XML.
        android.view.View customOrderButton =
                findViewById(R.id.custom_order_btn);

        if (customOrderButton != null) {

            customOrderButton.setOnClickListener(v -> {

                Intent intent =
                        new Intent(
                                MenuActivity.this,
                                CustomOrderActivity.class
                        );

                startActivity(intent);
            });
        }

        // WhatsApp is a TextView in your XML.
        TextView whatsappButton =
                findViewById(R.id.whatsapp_btn);

        if (whatsappButton != null) {

            whatsappButton.setOnClickListener(v ->
                    openWhatsApp()
            );
        }
    }

    private void addDishToCart(Dish dish) {

        if (dish == null) {
            return;
        }

        if (!dish.available) {

            Toast.makeText(
                    this,
                    "This dish is currently unavailable.",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        CartManager.getInstance()
                .addItem(dish);

        Toast.makeText(
                this,
                dish.name + " added to cart",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void loadLocalMenu() {

        allDishes.clear();

        allDishes.add(new Dish(
                "fufu-ewedu",
                "Fufu & Ewedu",
                2500,
                "Traditional Nigerian fufu served with fresh ewedu soup.",
                "images",
                4.8,
                24,
                25,
                true
        ));

        allDishes.add(new Dish(
                "jollof-chicken",
                "Jollof Rice & Chicken",
                3000,
                "Delicious Nigerian jollof rice served with chicken.",
                "images8",
                4.9,
                31,
                30,
                true
        ));

        allDishes.add(new Dish(
                "goat-pepper",
                "Goat Meat Pepper Sauce",
                3500,
                "Tender goat meat cooked in rich spicy pepper sauce.",
                "images13",
                4.7,
                19,
                35,
                true
        ));

        allDishes.add(new Dish(
                "creamy-pasta",
                "Creamy Pasta & Chicken",
                3500,
                "Creamy continental pasta served with seasoned chicken.",
                "images234",
                4.8,
                22,
                30,
                true
        ));

        allDishes.add(new Dish(
                "moi-moi",
                "Moi Moi",
                1500,
                "Steamed Nigerian bean pudding prepared with fresh ingredients.",
                "images10",
                4.6,
                17,
                25,
                true
        ));

        allDishes.add(new Dish(
                "pepper-soup",
                "Pepper Soup",
                2000,
                "Hot and spicy Nigerian pepper soup.",
                "images11",
                4.8,
                28,
                25,
                true
        ));

        allDishes.add(new Dish(
                "pounded-egusi",
                "Pounded Yam & Egusi",
                4000,
                "Smooth pounded yam served with rich egusi soup.",
                "images13",
                4.9,
                35,
                35,
                true
        ));

        allDishes.add(new Dish(
                "rice-vegetable",
                "Rice & Vegetable",
                2200,
                "Delicious rice served with fresh mixed vegetables.",
                "images15",
                4.7,
                21,
                25,
                true
        ));

        displayedDishes.clear();
        displayedDishes.addAll(allDishes);

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void loadLiveMenu() {

        executorService.execute(() -> {

            HttpURLConnection connection = null;

            try {

                URL url =
                        new URL(MENU_API);

                connection =
                        (HttpURLConnection)
                                url.openConnection();

                connection.setRequestMethod("GET");
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(15000);

                connection.setRequestProperty(
                        "Accept",
                        "application/json"
                );

                int responseCode =
                        connection.getResponseCode();

                if (responseCode !=
                        HttpURLConnection.HTTP_OK) {

                    throw new Exception(
                            "HTTP " + responseCode
                    );
                }

                InputStream inputStream =
                        connection.getInputStream();

                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(
                                        inputStream
                                )
                        );

                StringBuilder response =
                        new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                inputStream.close();

                JSONObject json =
                        new JSONObject(
                                response.toString()
                        );

                if (!json.optBoolean(
                        "success",
                        false
                )) {

                    throw new Exception(
                            "Menu API returned an error."
                    );
                }

                JSONArray items =
                        json.optJSONArray("items");

                if (items == null) {

                    throw new Exception(
                            "No menu items found."
                    );
                }

                List<Dish> liveDishes =
                        new ArrayList<>();

                for (int i = 0;
                     i < items.length();
                     i++) {

                    JSONObject item =
                            items.getJSONObject(i);

                    String dishId =
                            item.optString(
                                    "dish_id",
                                    String.valueOf(
                                            item.optInt("id")
                                    )
                            );

                    String name =
                            item.optString(
                                    "name",
                                    "Unnamed Dish"
                            );

                    double price =
                            item.optDouble(
                                    "price",
                                    0
                            );

                    String description =
                            item.optString(
                                    "description",
                                    ""
                            );

                    String imageUrl =
                            item.optString(
                                    "image_url",
                                    ""
                            );

                    boolean available =
                            item.optInt(
                                    "available",
                                    1
                            ) == 1;

                    Dish dish =
                            new Dish(
                                    dishId,
                                    name,
                                    (int) price,
                                    description,
                                    imageUrl,
                                    0,
                                    0,
                                    0,
                                    available
                            );

                    liveDishes.add(dish);
                }

                runOnUiThread(() -> {

                    allDishes.clear();
                    allDishes.addAll(liveDishes);

                    displayedDishes.clear();
                    displayedDishes.addAll(liveDishes);

                    adapter.notifyDataSetChanged();
                });

            } catch (Exception e) {

                runOnUiThread(() -> {

                    Toast.makeText(
                            MenuActivity.this,
                            "Using offline menu",
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

    private void showCategory(String category) {

        displayedDishes.clear();

        if (category.equalsIgnoreCase("all")) {

            displayedDishes.addAll(
                    allDishes
            );

        } else {

            for (Dish dish : allDishes) {

                String dishCategory =
                        getDishCategory(dish.id);

                if (dishCategory.equalsIgnoreCase(
                        category
                )) {

                    displayedDishes.add(dish);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    private String getDishCategory(String id) {

        if (id == null) {
            return "";
        }

        switch (id) {

            case "fufu-ewedu":
            case "moi-moi":
            case "pounded-egusi":
                return "African";

            case "creamy-pasta":
                return "Continental";

            case "goat-pepper":
                return "Grills";

            case "jollof-chicken":
            case "rice-vegetable":
                return "Rice";

            case "pepper-soup":
                return "Soups";

            default:
                return "";
        }
    }

    private void openWhatsApp() {

        try {

            String phone =
                    "2347089364492";

            String message =
                    "Hello IyaAdam Kitchen, I would like to make an enquiry.";

            String url =
                    "https://wa.me/" +
                    phone +
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
                    "WhatsApp is not available.",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}
