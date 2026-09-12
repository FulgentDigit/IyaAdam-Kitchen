package com.iyaadam.app;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dishesRecycler = findViewById(R.id.dishes_recycler);
        dishesRecycler.setLayoutManager(new GridLayoutManager(this, 2));

        dishes = createSampleDishes();

        adapter = new DishAdapter(this, dishes, this);
        dishesRecycler.setAdapter(adapter);

        findViewById(R.id.ic_search).setOnClickListener(v ->
                openMenu());

        findViewById(R.id.btn_order_now).setOnClickListener(v ->
                openMenu());

        findViewById(R.id.btn_view_all).setOnClickListener(v ->
                openMenu());

        findViewById(R.id.ic_cart_nav).setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        findViewById(R.id.ic_cart_header).setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        findViewById(R.id.ic_person).setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class)));

        findViewById(R.id.ic_orders).setOnClickListener(v ->
                startActivity(new Intent(this, MusicPlayerActivity.class)));

        findViewById(R.id.ic_home).setOnClickListener(v ->
                Toast.makeText(this,
                        "Welcome to IyaAdam Kitchen!",
                        Toast.LENGTH_SHORT).show());

        findViewById(R.id.ic_notification).setOnClickListener(v ->
                Toast.makeText(this,
                        "No new notifications",
                        Toast.LENGTH_SHORT).show());
    }

    private void openMenu() {
        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    private List<Dish> createSampleDishes() {

        List<Dish> list = new ArrayList<>();

        list.add(new Dish(
                "1",
                "Fufu & Ewedu",
                2500,
                "Traditional African delicacy",
                "images",
                4.7,
                152,
                20,
                true));

        list.add(new Dish(
                "2",
                "Jollof Rice & Chicken",
                3000,
                "Classic Nigerian taste",
                "images8",
                4.8,
                198,
                25,
                true));

        list.add(new Dish(
                "3",
                "Goat Meat Pepper Sauce",
                3500,
                "Tender goat in spicy sauce",
                "images13",
                4.6,
                87,
                30,
                true));

        list.add(new Dish(
                "4",
                "Creamy Pasta & Chicken",
                3500,
                "Rich and satisfying",
                "images234",
                4.5,
                124,
                15,
                true));

        list.add(new Dish(
                "5",
                "Moi Moi",
                1500,
                "Steamed bean pudding",
                "images10",
                4.4,
                76,
                20,
                true));

        list.add(new Dish(
                "6",
                "Pepper Soup",
                2000,
                "Hot flavorful broth",
                "images11",
                4.3,
                95,
                15,
                true));

        list.add(new Dish(
                "7",
                "Pounded Yam & Egusi",
                4000,
                "Smooth yam with gravy",
                "images13",
                4.9,
                213,
                25,
                true));

        list.add(new Dish(
                "8",
                "Rice & Vegetable",
                2200,
                "Mixed vegetable rice",
                "images15",
                4.2,
                64,
                18,
                true));

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
