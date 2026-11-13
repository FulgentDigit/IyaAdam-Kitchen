package com.iyaadam.app;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class DishAdapter
        extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {

    private final Context context;
    private final List<Dish> dishes;
    private final OnDishClickListener listener;

    private final int placeholder =
            android.R.drawable.ic_menu_gallery;

    public interface OnDishClickListener {

        void onDishClick(Dish dish);

        void onAddToCart(Dish dish);
    }

    public DishAdapter(
            Context context,
            List<Dish> dishes,
            OnDishClickListener listener
    ) {
        this.context = context;
        this.dishes = dishes;
        this.listener = listener;
    }

    @Override
    public DishViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType
    ) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.dish_card,
                parent,
                false
        );

        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            DishViewHolder holder,
            int position
    ) {

        Dish dish = dishes.get(position);

        holder.dishName.setText(dish.name);

        holder.dishPrice.setText(
                "₦" + formatPrice(dish.price)
        );

        holder.dishDescription.setText(
                dish.description == null
                        ? ""
                        : dish.description
        );

        holder.prepTime.setText(
                dish.prepTime + " mins"
        );

        holder.rating.setText(
                String.format(
                        Locale.getDefault(),
                        "%.1f ⭐ (%d)",
                        dish.rating,
                        dish.reviewCount
                )
        );

        loadImage(
                holder.dishImage,
                dish.imageUrl,
                dish.name
        );

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

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    private String formatPrice(double price) {

        if (price == (long) price) {
            return String.valueOf((long) price);
        }

        return String.valueOf(price);
    }

    /*
     * LOCAL DRAWABLE IMAGE LOADING ONLY.
     * No server downloads or internet image requests.
     */
    private void loadImage(
            ImageView imageView,
            String imageSource,
            String dishName
    ) {

        imageView.setImageResource(placeholder);

        // First try the drawable name supplied for the dish.
        String drawableName = getSourceDrawableName(imageSource);

        int resourceId = findDrawable(drawableName);

        // If the supplied name does not exist,
        // use the dish name to find its local drawable.
        if (resourceId == 0) {
            drawableName = getDishDrawableName(dishName);
            resourceId = findDrawable(drawableName);
        }

        if (resourceId != 0) {
            imageView.setImageResource(resourceId);
        }
    }

    /*
     * Extracts a drawable filename from either:
     * images8
     * @drawable/images8
     * https://example.com/uploads/menu/images8.jpg
     */
    private String getSourceDrawableName(String source) {

        if (source == null || source.trim().isEmpty()) {
            return "";
        }

        String name = source.trim();

        if (name.startsWith("http://") ||
                name.startsWith("https://")) {

            try {
                String lastPart =
                        Uri.parse(name).getLastPathSegment();

                if (lastPart == null) {
                    return "";
                }

                name = lastPart;

            } catch (Exception ignored) {
                return "";
            }
        }

        if (name.startsWith("@drawable/")) {
            name = name.substring("@drawable/".length());
        }

        if (name.contains("/")) {
            name = name.substring(name.lastIndexOf("/") + 1);
        }

        int dot = name.lastIndexOf(".");

        if (dot > 0) {
            name = name.substring(0, dot);
        }

        return name.trim().toLowerCase(Locale.ROOT);
    }

    /*
     * Matches dish names to drawable files already
     * present in the Android project.
     */
    private String getDishDrawableName(String dishName) {

        if (dishName == null) {
            return "";
        }

        String name = dishName.toLowerCase(Locale.ROOT);

        if (name.contains("amala") ||
                name.contains("ewedu")) {
            return "amarla_with_ewedu";
        }

        if (name.contains("pounded yam") &&
                name.contains("egusi")) {
            return "pounded_yam_with_egusi_soup";
        }

        if (name.contains("pounded yam") &&
                name.contains("fish")) {
            return "pounded_yam_with_dried_fish";
        }

        if (name.contains("pounded yam") &&
                name.contains("chicken")) {
            return "pounded_yam_chicken";
        }

        if (name.contains("goat meat") &&
                (name.contains("pepper") ||
                        name.contains("soup"))) {
            return "goat_meat_pepper_soup";
        }

        if (name.contains("pepper soup")) {
            return "chicken_pepper_soup";
        }

        if (name.contains("vegetable rice") ||
                name.contains("rice and vegetable")) {
            return "vegetable_rice_with_boiled_eggs";
        }

        if (name.contains("rice") &&
                name.contains("egg")) {
            return "rice_and_egg";
        }

        if (name.contains("egusi")) {
            return "egusi_soup";
        }

        if (name.contains("fufu")) {
            return "fufu_egusi_soup";
        }

        if (name.contains("jollof")) {
            return "images8";
        }

        if (name.contains("moi moi") ||
                name.contains("moi-moi")) {
            return "images10";
        }

        if (name.contains("pasta")) {
            return "images234";
        }

        return "";
    }

    /*
     * Loads a drawable using the same Android resource
     * lookup approach used by the homepage.
     */
    private int findDrawable(String resourceName) {

        if (resourceName == null ||
                resourceName.trim().isEmpty()) {
            return 0;
        }

        return context.getResources().getIdentifier(
                resourceName.trim(),
                "drawable",
                context.getPackageName()
        );
    }

    @Override
    public void onViewRecycled(DishViewHolder holder) {

        super.onViewRecycled(holder);

        holder.dishImage.setImageResource(placeholder);
    }

    public static class DishViewHolder
            extends RecyclerView.ViewHolder {

        TextView dishName;
        TextView dishPrice;
        TextView dishDescription;
        TextView prepTime;
        TextView rating;

        ImageView dishImage;

        Button addToCartBtn;

        public DishViewHolder(View itemView) {

            super(itemView);

            dishImage = itemView.findViewById(
                    R.id.dish_image
            );

            dishName = itemView.findViewById(
                    R.id.dish_name
            );

            dishPrice = itemView.findViewById(
                    R.id.dish_price
            );

            dishDescription = itemView.findViewById(
                    R.id.dish_description
            );

            prepTime = itemView.findViewById(
                    R.id.prep_time
            );

            rating = itemView.findViewById(
                    R.id.rating
            );

            addToCartBtn = itemView.findViewById(
                    R.id.btn_add_to_cart
            );
        }
    }
}
