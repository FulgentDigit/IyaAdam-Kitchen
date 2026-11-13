package com.iyaadam.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dishesRecycler = findViewById(R.id.dishes_recycler);

        dishesRecycler.setLayoutManager(
                new GridLayoutManager(this, 2)
        );

        dishes = createSampleDishes();

        adapter = new DishAdapter(this, dishes, this);
        dishesRecycler.setAdapter(adapter);

        connectHomepageButtons();
    }

    private void connectHomepageButtons() {

        // NOTIFICATIONS
        findViewById(R.id.ic_notifications)
                .setOnClickListener(v -> {

                    startActivity(new Intent(
                            MainActivity.this,
                            NotificationActivity.class
                    ));

                });

        // HEADER CART
        findViewById(R.id.ic_cart_header)
                .setOnClickListener(v -> openCart());

        // BOTTOM CART
        findViewById(R.id.ic_cart_nav)
                .setOnClickListener(v -> openCart());

        // ORDER NOW
        findViewById(R.id.btn_order_now)
                .setOnClickListener(v -> openMenu());

        // VIEW ALL
        findViewById(R.id.btn_view_all)
                .setOnClickListener(v -> openMenu());

        // CATEGORY BUTTONS
        int[] categoryIds = {
                R.id.category_african,
                R.id.category_continental,
                R.id.category_grills,
                R.id.category_rice,
                R.id.category_soups
        };

        for (int id : categoryIds) {

            View category = findViewById(id);

            category.setOnClickListener(v -> openMenu());
        }

        // BOTTOM MENU
        findViewById(R.id.ic_search)
                .setOnClickListener(v -> openMenu());

        // PROFILE
        findViewById(R.id.ic_person)
                .setOnClickListener(v -> {

                    startActivity(new Intent(
                            MainActivity.this,
                            ProfileActivity.class
                    ));

                });

        // MUSIC
        findViewById(R.id.ic_orders)
                .setOnClickListener(v -> {

                    startActivity(new Intent(
                            MainActivity.this,
                            MusicPlayerActivity.class
                    ));

                });

        // HOME
        findViewById(R.id.ic_home)
                .setOnClickListener(v -> {

                    dishesRecycler.smoothScrollToPosition(0);

                    Toast.makeText(
                            this,
                            "Welcome to IyaAdam Kitchen!",
                            Toast.LENGTH_SHORT
                    ).show();

                });
    }

    private void openMenu() {

        startActivity(new Intent(
                MainActivity.this,
                MenuActivity.class
        ));
    }

    private void openCart() {

        startActivity(new Intent(
                MainActivity.this,
                CartActivity.class
        ));
    }

    private List<Dish> createSampleDishes() {

        List<Dish> list = new ArrayList<>();

        list.add(new Dish(
                "1",
                "Fufu & Ewedu",
                2500,
                "Traditional African delicacy",
                "amarla_with_ewedu",
                4.7,
                152,
                20,
                true
        ));

        list.add(new Dish(
                "2",
                "Goat Meat Pepper Soup",
                3500,
                "Tender goat meat in spicy soup",
                "goat_meat_pepper_soup",
                4.6,
                87,
                30,
                true
        ));

        list.add(new Dish(
                "3",
                "Egusi Soup with Assorted Meat",
                3500,
                "Rich egusi with assorted meat and fish",
                "egusi_soup_with_assorted_meat_and_fish",
                4.8,
                198,
                25,
                true
        ));

        list.add(new Dish(
                "4",
                "Pounded Yam & Egusi",
                4000,
                "Smooth pounded yam with egusi soup",
                "pounded_yam_with_egusi_soup",
                4.9,
                213,
                25,
                true
        ));

        list.add(new Dish(
                "5",
                "Chicken Pepper Soup",
                3000,
                "Freshly prepared spicy chicken soup",
                "chicken_pepper_soup",
                4.5,
                124,
                20,
                true
        ));

        list.add(new Dish(
                "6",
                "Rice & Egg",
                2200,
                "Delicious rice served with egg",
                "rice_and_egg",
                4.4,
                76,
                20,
                true
        ));

        list.add(new Dish(
                "7",
                "Vegetable Rice & Boiled Eggs",
                2800,
                "Nutritious vegetable rice with eggs",
                "vegetable_rice_with_boiled_eggs",
                4.7,
                95,
                20,
                true
        ));

        list.add(new Dish(
                "8",
                "Pounded Yam & Dried Fish",
                3800,
                "Traditional pounded yam with dried fish",
                "pounded_yam_and_dry_fish",
                4.6,
                110,
                25,
                true
        ));

        return list;
    }

    @Override
    public void onDishClick(Dish dish) {

        CartManager.getInstance().addToCart(dish);

        Toast.makeText(
                this,
                dish.name + " added to cart",
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onAddToCart(Dish dish) {

        CartManager.getInstance().addToCart(dish);

        Toast.makeText(
                this,
                dish.name + " added to cart",
                Toast.LENGTH_SHORT
        ).show();
    }
}
