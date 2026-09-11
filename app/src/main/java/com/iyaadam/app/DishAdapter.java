package com.iyaadam.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {

    private Context context;
    private List<Dish> dishes;
    private OnDishClickListener listener;

    public interface OnDishClickListener {
        void onDishClick(Dish dish);
        void onAddToCart(Dish dish);
    }

    public DishAdapter(Context context, List<Dish> dishes, OnDishClickListener listener) {
        this.context = context;
        this.dishes = dishes;
        this.listener = listener;
    }

    @Override
    public DishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dish_card, parent, false);
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DishViewHolder holder, int position) {
        Dish dish = dishes.get(position);
        
        holder.dishName.setText(dish.name);
        holder.dishPrice.setText("₦" + dish.price);
        holder.dishDescription.setText(dish.description);
        holder.prepTime.setText(dish.prepTime + " mins");
        holder.rating.setText(String.format("%.1f ⭐ (%d)", dish.rating, dish.reviewCount));
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDishClick(dish);
            }
        });
        
        holder.addToCartBtn.setOnClickListener(v -> {
            CartManager.getInstance().addToCart(dish);
            Toast.makeText(context, "✓ " + dish.name + " added to cart!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    private void loadImageFromUrl(ImageView imageView, String url) {
        imageView.setBackgroundColor(0xFF9E8B7E);
    }

    public static class DishViewHolder extends RecyclerView.ViewHolder {
        TextView dishName, dishPrice, dishDescription, prepTime, rating;
        ImageView dishImage;
        Button addToCartBtn;

        public DishViewHolder(View itemView) {
            super(itemView);
            dishImage = itemView.findViewById(R.id.dish_image);
            dishName = itemView.findViewById(R.id.dish_name);
            dishPrice = itemView.findViewById(R.id.dish_price);
            dishDescription = itemView.findViewById(R.id.dish_description);
            prepTime = itemView.findViewById(R.id.prep_time);
            rating = itemView.findViewById(R.id.rating);
            addToCartBtn = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }
}
