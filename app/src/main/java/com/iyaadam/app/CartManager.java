package com.iyaadam.app;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;
    private int totalPrice = 0;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addToCart(Dish dish) {
        // Check if dish already exists
        for (CartItem item : cartItems) {
            if (item.dish.id.equals(dish.id)) {
                item.quantity++;
                updateTotal();
                return;
            }
        }
        // New dish
        cartItems.add(new CartItem(dish, 1));
        updateTotal();
    }

    public void removeFromCart(String dishId) {
        cartItems.removeIf(item -> item.dish.id.equals(dishId));
        updateTotal();
    }

    public void updateQuantity(String dishId, int quantity) {
        for (CartItem item : cartItems) {
            if (item.dish.id.equals(dishId)) {
                item.quantity = Math.max(0, quantity);
                if (item.quantity == 0) {
                    removeFromCart(dishId);
                }
                updateTotal();
                return;
            }
        }
    }

    private void updateTotal() {
        totalPrice = 0;
        for (CartItem item : cartItems) {
            totalPrice += item.dish.price * item.quantity;
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getItemCount() {
        return cartItems.size();
    }

    public void clearCart() {
        cartItems.clear();
        totalPrice = 0;
    }

    public static class CartItem {
        public Dish dish;
        public int quantity;

        public CartItem(Dish dish, int quantity) {
            this.dish = dish;
            this.quantity = quantity;
        }

        public int getSubtotal() {
            return dish.price * quantity;
        }
    }
}
