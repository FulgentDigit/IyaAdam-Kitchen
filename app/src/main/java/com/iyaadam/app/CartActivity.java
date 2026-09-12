package com.iyaadam.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartChangeListener {

    private RecyclerView cartRecycler;
    private CartAdapter adapter;
    private TextView totalPriceTV, emptyMsg;
    private Button checkoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecycler = findViewById(R.id.cart_recycler);
        totalPriceTV = findViewById(R.id.total_price);
        emptyMsg = findViewById(R.id.empty_msg);
        checkoutBtn = findViewById(R.id.checkout_btn);

        cartRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(this, CartManager.getInstance().getCartItems(), this);
        cartRecycler.setAdapter(adapter);

        updateUI();

        checkoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onCartChanged() {
        updateUI();
    }

    private void updateUI() {
        int itemCount = CartManager.getInstance().getItemCount();
        int totalPrice = CartManager.getInstance().getTotalPrice();

        totalPriceTV.setText("₦" + totalPrice);

        if (itemCount == 0) {
            checkoutBtn.setEnabled(false);
            emptyMsg.setVisibility(View.VISIBLE);
            emptyMsg.setText("Your cart is empty");
            cartRecycler.setVisibility(View.GONE);
        } else {
            checkoutBtn.setEnabled(true);
            emptyMsg.setVisibility(View.GONE);
            cartRecycler.setVisibility(View.VISIBLE);
        }
    }
}
