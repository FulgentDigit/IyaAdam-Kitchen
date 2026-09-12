package com.iyaadam.app;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    private RecyclerView menuRecycler;
    private DishAdapter adapter;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(v -> finish());

        menuRecycler = findViewById(R.id.menu_recycler);
        menuRecycler.setLayoutManager(new LinearLayoutManager(this));

        List<Dish> allDishes = createAllDishes();
        adapter = new DishAdapter(this, allDishes, dish -> CartManager.getInstance().addToCart(dish));
        menuRecycler.setAdapter(adapter);
    }

    private List<Dish> createAllDishes() {
        List<Dish> dishes = new ArrayList<>();
        dishes.add(new Dish("1", "Fufu & Ewedu", 2500, "Traditional leafy soup", "images", 4.7, 152, 20, true));
        dishes.add(new Dish("2", "Jollof Rice & Chicken", 3000, "Spiced rice perfection", "images8", 4.8, 198, 25, true));
        dishes.add(new Dish("3", "Goat Meat Pepper Sauce", 3500, "Tender meat in spicy broth", "goat_meat_pepper_soup", 4.6, 87, 30, true));
        dishes.add(new Dish("4", "Creamy Pasta & Chicken", 3500, "Italian meets African", "images234", 4.5, 124, 15, true));
        dishes.add(new Dish("5", "Moi Moi", 1500, "Steamed bean pudding", "images10", 4.4, 76, 20, true));
        dishes.add(new Dish("6", "Pepper Soup", 2000, "Hot traditional broth", "images11", 4.3, 95, 15, true));
        dishes.add(new Dish("7", "Pounded Yam & Egusi", 4000, "Smooth yam with soup", "images13", 4.9, 213, 25, true));
        dishes.add(new Dish("8", "Rice & Vegetable", 2200, "Creamy mixed vegetables", "images15", 4.2, 64, 18, true));
        dishes.add(new Dish("9", "Amala & Ewedu", 2800, "Cornmeal with leafy soup", "amala_with_ewedu_and_goat_meat", 4.6, 141, 22, true));
        dishes.add(new Dish("10", "Egusi Soup", 2200, "Melon seed delicacy", "egusi_soup", 4.5, 108, 18, true));
        return dishes;
    }
}
