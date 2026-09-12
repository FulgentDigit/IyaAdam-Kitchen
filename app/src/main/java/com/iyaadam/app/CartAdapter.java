package com.iyaadam.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartManager.CartItem> cartItems;
    private OnCartChangeListener listener;

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    public CartAdapter(
            Context context,
            List<CartManager.CartItem> cartItems,
            OnCartChangeListener listener) {

        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @Override
    public CartViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.cart_item, parent, false);

        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            CartViewHolder holder,
            int position) {

        CartManager.CartItem cartItem = cartItems.get(position);
        Dish dish = cartItem.dish;

        holder.dishName.setText(dish.name);
        holder.dishPrice.setText("₦" + dish.price);
        holder.subtotal.setText("₦" + cartItem.getSubtotal());
        holder.quantity.setText(String.valueOf(cartItem.quantity));

        // LOAD DISH IMAGE
        try {
            int imageRes = context.getResources().getIdentifier(
                    dish.imageUrl,
                    "drawable",
                    context.getPackageName()
            );

            if (imageRes != 0) {
                holder.dishImage.setImageResource(imageRes);
            } else {
                holder.dishImage.setImageResource(
                        android.R.drawable.ic_menu_gallery
                );
            }

        } catch (Exception e) {
            holder.dishImage.setImageResource(
                    android.R.drawable.ic_menu_gallery
            );
        }

        holder.btnDecrease.setOnClickListener(v -> {

            CartManager.getInstance().updateQuantity(
                    dish.id,
                    cartItem.quantity - 1
            );

            notifyDataSetChanged();

            if (listener != null) {
                listener.onCartChanged();
            }
        });

        holder.btnIncrease.setOnClickListener(v -> {

            CartManager.getInstance().updateQuantity(
                    dish.id,
                    cartItem.quantity + 1
            );

            notifyDataSetChanged();

            if (listener != null) {
                listener.onCartChanged();
            }
        });

        holder.btnRemove.setOnClickListener(v -> {

            CartManager.getInstance().removeFromCart(dish.id);

            notifyDataSetChanged();

            if (listener != null) {
                listener.onCartChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder
            extends RecyclerView.ViewHolder {

        TextView dishName;
        TextView dishPrice;
        TextView subtotal;
        TextView quantity;

        Button btnDecrease;
        Button btnIncrease;
        Button btnRemove;

        ImageView dishImage;

        public CartViewHolder(View itemView) {
            super(itemView);

            dishImage = itemView.findViewById(R.id.dish_image);
            dishName = itemView.findViewById(R.id.dish_name);
            dishPrice = itemView.findViewById(R.id.dish_price);
            subtotal = itemView.findViewById(R.id.subtotal);
            quantity = itemView.findViewById(R.id.quantity);

            btnDecrease = itemView.findViewById(R.id.btn_decrease);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }
    }
}
