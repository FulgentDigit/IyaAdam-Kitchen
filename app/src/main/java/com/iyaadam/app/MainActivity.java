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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup RecyclerView
        dishesRecycler = findViewById(R.id.dishes_recycler);
        dishesRecycler.setLayoutManager(new GridLayoutManager(this, 2));

        // Create sample dishes
        dishes = createSampleDishes();

        // Setup adapter
        adapter = new DishAdapter(this, dishes, this);
        dishesRecycler.setAdapter(adapter);

        // Navigation buttons
        ImageButton cartButton = findViewById(R.id.ic_cart);
        ImageButton accountButton = findViewById(R.id.ic_person);
        ImageButton searchButton = findViewById(R.id.ic_search);
        ImageButton musicButton = findViewById(R.id.ic_orders);  // Music button in bottom nav

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

        // Music Player Button
        musicButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
            startActivity(intent);
        });
    }

    private List<Dish> createSampleDishes() {
        List<Dish> dishList = new ArrayList<>();
        
        dishList.add(new Dish("1", "Jollof Rice Special", 2500, "Aromatic rice with spices", 
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=400", 4.7, 152, 20, true));
        
        dishList.add(new Dish("2", "Grilled Chicken", 3000, "Perfectly seasoned chicken breast", 
            "https://images.unsplash.com/photo-1598103442097-8b74394b95c6?w=400", 4.8, 198, 25, true));
        
        dishList.add(new Dish("3", "Goat Meat Pepper Sauce", 3500, "Tender goat in spicy sauce", 
            "https://images.unsplash.com/photo-1626082927389-6cd097cfd330?w=400", 4.6, 87, 30, true));
        
        dishList.add(new Dish("4", "Suya Beef", 2800, "Spiced roasted beef strips", 
            "https://images.unsplash.com/photo-1555939594-58d7cb561404?w=400", 4.5, 124, 15, true));
        
        dishList.add(new Dish("5", "Moi Moi", 1500, "Steamed bean pudding", 
            "https://images.unsplash.com/photo-1609501676725-7186f017a4b5?w=400", 4.4, 76, 20, true));
        
        dishList.add(new Dish("6", "Pepper Soup", 2000, "Hot & flavorful broth", 
            "https://images.unsplash.com/photo-1547592166-23ac45744acd?w=400", 4.3, 95, 15, true));
        
        dishList.add(new Dish("7", "Pounded Yam with Egusi", 4000, "Smooth yam with egusi gravy", 
            "https://images.unsplash.com/photo-1596103442097-8b74394b95c6?w=400", 4.9, 213, 25, true));
        
        dishList.add(new Dish("8", "Tuwo de Rice", 2200, "Creamy savory rice meal", 
            "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=400", 4.2, 64, 18, true));

        return dishList;
    }

    @Override
    public void onDishClick(Dish dish) {
        Toast.makeText(this, "Viewing: " + dish.name, Toast.LENGTH_SHORT).show();
        // TODO: Open dish detail screen
    }

    @Override
    public void onAddToCart(Dish dish) {
        // TODO: Add to cart logic
        Toast.makeText(this, dish.name + " added to cart!", Toast.LENGTH_SHORT).show();
    }
}
