package com.iyaadam.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {

    private final Context context;
    private final List<Dish> dishes;
    private final OnDishClickListener listener;

    public interface OnDishClickListener {
        void onDishClick(Dish dish);
        void onAddToCart(Dish dish);
    }

    public DishAdapter(Context context, List<Dish> dishes,
                       OnDishClickListener listener) {
        this.context = context;
        this.dishes = dishes;
        this.listener = listener;
    }

    @Override
    public DishViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.dish_card, parent, false
        );
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DishViewHolder holder, int position) {
        Dish dish = dishes.get(position);

        holder.dishName.setText(dish.name);
        holder.dishPrice.setText("₦" + formatPrice(dish.price));
        holder.dishDescription.setText(
                dish.description == null ? "" : dish.description
        );
        holder.prepTime.setText(dish.prepTime + " mins");

        holder.rating.setText(String.format(
                Locale.getDefault(),
                "%.1f ⭐ (%d)",
                dish.rating,
                dish.reviewCount
        ));

        loadLocalImage(holder.dishImage, dish.imageUrl);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDishClick(dish);
            }
        });

        holder.addToCartBtn.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddToCart(dish);
            }
        });
    }

    private void loadLocalImage(ImageView imageView, String imageName) {
        imageView.setImageResource(android.R.drawable.ic_menu_gallery);

        if (imageName == null || imageName.trim().isEmpty()) {
            return;
        }

        String name = imageName.trim();

        if (name.startsWith("@drawable/")) {
            name = name.substring("@drawable/".length());
        }

        int resourceId = context.getResources().getIdentifier(
                name,
                "drawable",
                context.getPackageName()
        );

        if (resourceId != 0) {
            imageView.setImageResource(resourceId);
        }
    }

    private String formatPrice(double price) {
        if (price == (long) price) {
            return String.valueOf((long) price);
        }
        return String.valueOf(price);
    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    @Override
    public void onViewRecycled(DishViewHolder holder) {
        super.onViewRecycled(holder);
        holder.dishImage.setImageResource(
                android.R.drawable.ic_menu_gallery
        );
    }

    public static class DishViewHolder extends RecyclerView.ViewHolder {

        TextView dishName;
        TextView dishPrice;
        TextView dishDescription;
        TextView prepTime;
        TextView rating;
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
