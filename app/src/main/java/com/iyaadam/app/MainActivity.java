package com.iyaadam.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements DishAdapter.OnDishClickListener {

    private RecyclerView dishesRecycler;
    private DishAdapter adapter;
    private List<Dish> dishes;

    private TextView cartButton;
    private TextView accountButton;
    private TextView searchButton;
    private TextView musicButton;
    private TextView homeButton;
    private TextView notificationButton;
    private TextView orderNowButton;
    private TextView viewAllButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FEATURED DISHES
        dishesRecycler = findViewById(R.id.dishes_recycler);
        dishesRecycler.setLayoutManager(new GridLayoutManager(this, 2));

        dishes = createSampleDishes();
        adapter = new DishAdapter(this, dishes, this);
        dishesRecycler.setAdapter(adapter);

        // BOTTOM NAVIGATION
        homeButton = findViewById(R.id.ic_home);
        searchButton = findViewById(R.id.ic_search);
        cartButton = findViewById(R.id.ic_cart_nav);
        musicButton = findViewById(R.id.ic_orders);
        accountButton = findViewById(R.id.ic_person);

        // HEADER
        notificationButton = findViewById(R.id.ic_notification);
        TextView headerCartButton = findViewById(R.id.ic_cart_header);

        // HERO
        orderNowButton = findViewById(R.id.btn_order_now);

        // FEATURED
        viewAllButton = findViewById(R.id.btn_view_all);

        // MENU
        searchButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MenuActivity.class));
        });

        // CART - BOTTOM
        cartButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        });

        // CART - HEADER
        headerCartButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        });

        // NOTIFICATIONS
        notificationButton.setOnClickListener(v -> {
            Toast.makeText(
                    MainActivity.this,
                    "You have 1 new notification",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // ORDER NOW
        orderNowButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MenuActivity.class));
        });

        // VIEW ALL
        viewAllButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MenuActivity.class));
        });

        // PROFILE
        accountButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        });

        // MUSIC
        musicButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MusicPlayerActivity.class));
        });

        // HOME
        homeButton.setOnClickListener(v -> {
            Toast.makeText(
                    MainActivity.this,
                    "Welcome to IyaAdam Kitchen!",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    private List<Dish> createSampleDishes() {

        List<Dish> dishList = new ArrayList<>();

        // 1
        dishList.add(new Dish(
                "1",
                "Fufu & Ewedu",
                2500,
                "Traditional African delicacy",
                "images",
                4.7,
                152,
                20,
                true
        ));

        // 2
        dishList.add(new Dish(
                "2",
                "Jollof Rice & Chicken",
                3000,
                "Classic Nigerian taste",
                "images8",
                4.8,
                198,
                25,
                true
        ));

        // 3 - FIXED IMAGE
        dishList.add(new Dish(
                "3",
                "Creamy Pasta & Chicken",
                3500,
                "Rich and satisfying",
                "images234",
                4.5,
                124,
                15,
                true
        ));

        // 4
        dishList.add(new Dish(
                "4",
                "Goat Meat Pepper Sauce",
                3500,
                "Tender goat in spicy sauce",
                "images13",
                4.6,
                87,
                30,
                true
        ));

        // 5
        dishList.add(new Dish(
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

        // 6
        dishList.add(new Dish(
                "6",
                "Pepper Soup",
                2000,
                "Hot flavorful broth",
                "images11",
                4.3,
                95,
                15,
                true
        ));

        // 7
        dishList.add(new Dish(
                "7",
                "Pounded Yam & Egusi",
                4000,
                "Smooth yam with gravy",
                "images13",
                4.9,
                213,
                25,
                true
        ));

        // 8
        dishList.add(new Dish(
                "8",
                "Rice & Vegetable",
                2200,
                "Tasty vegetable rice",
                "images15",
                4.2,
                64,
                18,
                true
        ));

        return dishList;
    }

    @Override
    public void onDishClick(Dish dish) {
        Toast.makeText(
                this,
                "Viewing: " + dish.name,
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onAddToCart(Dish dish) {
        CartManager.getInstance().addToCart(dish);

        Toast.makeText(
                this,
                "✓ " + dish.name + " added to cart!",
                Toast.LENGTH_SHORT
        ).show();
    }
}
