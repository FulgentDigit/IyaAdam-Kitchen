package com.iyaadam.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DishAdapter.OnDishClickListener {

    private RecyclerView dishesRecycler;
    private DishAdapter adapter;
    private List<Dish> dishes;
    private ImageButton cartButton, accountButton, searchButton, musicButton, howToOrderButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dishesRecycler = findViewById(R.id.dishes_recycler);
        dishesRecycler.setLayoutManager(new GridLayoutManager(this, 2));

        dishes = createSampleDishes();

        adapter = new DishAdapter(this, dishes, this);
        dishesRecycler.setAdapter(adapter);

        cartButton = findViewById(R.id.ic_cart_nav);
        accountButton = findViewById(R.id.ic_person);
        searchButton = findViewById(R.id.ic_search);
        musicButton = findViewById(R.id.ic_orders);
        howToOrderButton = findViewById(R.id.ic_home);

        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });

        accountButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AccountActivity.class);
            startActivity(intent);
        });

        searchButton.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Search feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        musicButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
            startActivity(intent);
        });

        howToOrderButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HowToOrderActivity.class);
            startActivity(intent);
        });
    }

    private List<Dish> createSampleDishes() {
        List<Dish> dishList = new ArrayList<>();
        
        dishList.add(new Dish("1", "Fufu & Ewedu", 2500, "Traditional African delicacy with rich soup", 
            "images_image", 4.7, 152, 20, true));
        
        dishList.add(new Dish("2", "Jollof Rice & Chicken", 3000, "Classic Nigerian taste at its best", 
            "images8", 4.8, 198, 25, true));
        
        dishList.add(new Dish("3", "Goat Meat Pepper Sauce", 3500, "Tender goat in spicy sauce", 
            "goat_meat_pepper_soup", 4.6, 87, 30, true));
        
        dishList.add(new Dish("4", "Creamy Pasta & Chicken", 3500, "Rich, smooth and satisfying", 
            "images234", 4.5, 124, 15, true));

        dishList.add(new Dish("5", "Moi Moi", 1500, "Steamed bean pudding", 
            "images10", 4.4, 76, 20, true));
        
        dishList.add(new Dish("6", "Pepper Soup", 2000, "Hot & flavorful broth", 
            "images11", 4.3, 95, 15, true));
        
        dishList.add(new Dish("7", "Pounded Yam with Egusi", 4000, "Smooth yam with egusi gravy", 
            "images13", 4.9, 213, 25, true));
        
        dishList.add(new Dish("8", "Rice & Vegetable", 2200, "Creamy savory rice meal", 
            "images15", 4.2, 64, 18, true));

        return dishList;
    }

    @Override
    public void onDishClick(Dish dish) {
        Toast.makeText(this, "Viewing: " + dish.name, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddToCart(Dish dish) {
        CartManager.getInstance().addToCart(dish);
    }
}
