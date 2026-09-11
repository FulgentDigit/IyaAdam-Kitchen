package com.iyaadam.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartChangeListener {

    private RecyclerView cartRecycler;
    private CartAdapter adapter;
    private TextView totalPriceTV, itemCountTV;
    private Button checkoutBtn, continueShoppingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecycler = findViewById(R.id.cart_recycler);
        totalPriceTV = findViewById(R.id.total_price);
        itemCountTV = findViewById(R.id.item_count);
        checkoutBtn = findViewById(R.id.btn_checkout);
        continueShoppingBtn = findViewById(R.id.btn_continue_shopping);

        cartRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(this, CartManager.getInstance().getCartItems(), this);
        cartRecycler.setAdapter(adapter);

        updateUI();

        checkoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            startActivity(intent);
        });

        continueShoppingBtn.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void onCartChanged() {
        updateUI();
    }

    private void updateUI() {
        int itemCount = CartManager.getInstance().getItemCount();
        int totalPrice = CartManager.getInstance().getTotalPrice();
        
        itemCountTV.setText("Items: " + itemCount);
        totalPriceTV.setText("Total: ₦" + totalPrice);
        
        if (itemCount == 0) {
            checkoutBtn.setEnabled(false);
        } else {
            checkoutBtn.setEnabled(true);
        }
    }
}
