package com.iyaadam.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView menuRecycler;
    private List<Dish> allDishes;
    private DishAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ImageView backBtn = findViewById(R.id.back_btn);
        LinearLayout customOrderBtn = findViewById(R.id.custom_order_btn);
        TextView whatsappBtn = findViewById(R.id.whatsapp_btn);

        menuRecycler = findViewById(R.id.menu_recycler);

        backBtn.setOnClickListener(v -> finish());

        customOrderBtn.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MenuActivity.this,
                    CustomOrderActivity.class
            );
            startActivity(intent);
        });

        whatsappBtn.setOnClickListener(v -> openWhatsApp());

        menuRecycler.setLayoutManager(
                new LinearLayoutManager(this)
        );

        allDishes = createAllDishes();

        showDishes(allDishes);

        Button all = findViewById(R.id.category_all);
        Button african = findViewById(R.id.category_african);
        Button continental = findViewById(R.id.category_continental);
        Button grills = findViewById(R.id.category_grills);
        Button rice = findViewById(R.id.category_rice);
        Button soups = findViewById(R.id.category_soups);

        all.setOnClickListener(v -> showDishes(allDishes));

        african.setOnClickListener(v ->
                showDishesByIds(1, 3, 5, 6, 7));

        continental.setOnClickListener(v ->
                showDishesByIds(4, 8));

        grills.setOnClickListener(v ->
                showDishesByIds(3));

        rice.setOnClickListener(v ->
                showDishesByIds(2, 8));

        soups.setOnClickListener(v ->
                showDishesByIds(1, 3, 6, 7));
    }

    private void showDishes(List<Dish> dishes) {

        adapter = new DishAdapter(
                this,
                dishes,
                new DishAdapter.OnDishClickListener() {

                    @Override
                    public void onDishClick(Dish dish) {
                        addToCart(dish);
                    }

                    @Override
                    public void onAddToCart(Dish dish) {
                        addToCart(dish);
                    }
                }
        );

        menuRecycler.setAdapter(adapter);
    }

    private void showDishesByIds(int... ids) {

        List<Dish> filtered = new ArrayList<>();

        for (Dish dish : allDishes) {

            try {
                int dishId = Integer.parseInt(dish.id);

                for (int id : ids) {
                    if (dishId == id) {
                        filtered.add(dish);
                        break;
                    }
                }

            } catch (Exception ignored) {
            }
        }

        showDishes(filtered);
    }

    private void addToCart(Dish dish) {

        CartManager.getInstance().addToCart(dish);

        Toast.makeText(
                this,
                "✓ " + dish.name + " added to cart",
                Toast.LENGTH_SHORT
        ).show();
    }

    private void openWhatsApp() {

        String phone = "2347089364492";

        try {
            Intent intent = new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://wa.me/" + phone)
            );

            startActivity(intent);

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "WhatsApp is not available on this phone",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private List<Dish> createAllDishes() {

        List<Dish> dishes = new ArrayList<>();

        dishes.add(new Dish(
                "1",
                "Fufu & Ewedu",
                2500,
                "Traditional leafy soup",
                "images",
                4.7,
                152,
                20,
                true
        ));

        dishes.add(new Dish(
                "2",
                "Jollof Rice & Chicken",
                3000,
                "Spiced rice perfection",
                "images8",
                4.8,
                198,
                25,
                true
        ));

        dishes.add(new Dish(
                "3",
                "Goat Meat Pepper Sauce",
                3500,
                "Tender meat in spicy broth",
                "images13",
                4.6,
                87,
                30,
                true
        ));

        dishes.add(new Dish(
                "4",
                "Creamy Pasta & Chicken",
                3500,
                "Italian meets African",
                "images234",
                4.5,
                124,
                15,
                true
        ));

        dishes.add(new Dish(
                "5",
                "Moi Moi",
                1500,
                "Steamed bean pudding",
                "images10",
                4.4,
                76,
                20,
                true
        ));

        dishes.add(new Dish(
                "6",
                "Pepper Soup",
                2000,
                "Hot traditional broth",
                "images11",
                4.3,
                95,
                15,
                true
        ));

        dishes.add(new Dish(
                "7",
                "Pounded Yam & Egusi",
                4000,
                "Smooth yam with soup",
                "images13",
                4.9,
                213,
                25,
                true
        ));

        dishes.add(new Dish(
                "8",
                "Rice & Vegetable",
                2200,
                "Creamy mixed vegetables",
                "images15",
                4.2,
                64,
                18,
                true
        ));

        return dishes;
    }
}
