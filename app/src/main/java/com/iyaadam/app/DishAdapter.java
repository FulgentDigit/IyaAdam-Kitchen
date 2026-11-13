
package com.iyaadam.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.net.URLDecoder;
import java.util.ArrayList;
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

        List<String> candidates = new ArrayList<>();

        String name = imageName.trim();

        try {
            name = URLDecoder.decode(name, "UTF-8");
        } catch (Exception ignored) {
        }

        // Handle drawable references.
        if (name.startsWith("@drawable/")) {
            name = name.substring("@drawable/".length());
        }

        // Handle URLs and file paths.
        if (name.contains("/")) {
            name = name.substring(name.lastIndexOf("/") + 1);
        }

        // Remove file extension.
        int dot = name.lastIndexOf(".");
        if (dot > 0) {
            name = name.substring(0, dot);
        }

        name = name.toLowerCase(Locale.ROOT)
                .replace("-", "_")
                .replace(" ", "_")
                .replaceAll("[^a-z0-9_]", "")
                .replaceAll("_+", "_");

        addCandidate(candidates, name);

        // Common filename variations.
        if (name.equals("amarla_with_ewedu")) {
            addCandidate(candidates, "amala_with_ewedu");
            addCandidate(candidates, "amala_ewedu");
        }

        if (name.equals("pounded_yam_and_dry_fish")) {
            addCandidate(candidates, "pounded_yam_with_dried_fish");
            addCandidate(candidates, "pounded_yam_dry_fish");
        }

        if (name.equals("egusi_soup_with_assorted_meat_and_fish")) {
            addCandidate(candidates, "egusi_soup_assorted_meat_and_fish");
            addCandidate(candidates, "egusi_soup");
        }

        if (name.equals("pounded_yam_with_egusi_soup")) {
            addCandidate(candidates, "pounded_yam_egusi_soup");
        }

        if (name.equals("goat_meat_pepper_soup")) {
            addCandidate(candidates, "goat_pepper_soup");
        }

        if (name.equals("chicken_pepper_soup")) {
            addCandidate(candidates, "chicken_pepper_soup");
        }

        if (name.equals("rice_and_egg")) {
            addCandidate(candidates, "rice_with_egg");
        }

        if (name.equals("vegetable_rice_with_boiled_eggs")) {
            addCandidate(candidates, "vegetable_rice_boiled_eggs");
            addCandidate(candidates, "vegetable_rice");
        }

        // Search Android drawable resources.
        for (String candidate : candidates) {

            int resourceId = context.getResources().getIdentifier(
                    candidate,
                    "drawable",
                    context.getPackageName()
            );

            if (resourceId != 0) {
                imageView.setImageResource(resourceId);
                return;
            }
        }
    }

    private void addCandidate(List<String> candidates, String name) {
        if (name != null && !candidates.contains(name)) {
            candidates.add(name);
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
