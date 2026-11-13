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
        View view = LayoutInflater.from(context)
                .inflate(R.layout.dish_card, parent, false);
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

        holder.dishImage.setImageResource(getDishImage(dish.imageUrl));

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

    private int getDishImage(String imageName) {

        if (imageName == null) {
            return android.R.drawable.ic_menu_gallery;
        }

        switch (imageName) {

            case "amarla_with_ewedu":
                return R.drawable.amarla_with_ewedu;

            case "goat_meat_pepper_soup":
                return R.drawable.goat_meat_pepper_soup;

            case "egusi_soup_with_assorted_meat_and_fish":
                return R.drawable.egusi_soup_with_assorted_meat_and_fish;

            case "pounded_yam_with_egusi_soup":
                return R.drawable.pounded_yam_with_egusi_soup;

            case "chicken_pepper_soup":
                return R.drawable.chicken_pepper_soup;

            case "rice_and_egg":
                return R.drawable.rice_and_egg;

            case "vegetable_rice_with_boiled_eggs":
                return R.drawable.vegetable_rice_with_boiled_eggs;

            case "pounded_yam_and_dry_fish":
                return R.drawable.pounded_yam_and_dry_fish;

            default:
                return android.R.drawable.ic_menu_gallery;
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
