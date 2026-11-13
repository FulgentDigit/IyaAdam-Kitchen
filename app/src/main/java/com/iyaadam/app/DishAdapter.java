package com.iyaadam.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DishAdapter
        extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {

    private final Context context;
    private final List<Dish> dishes;
    private final OnDishClickListener listener;

    private final ExecutorService imageExecutor =
            Executors.newFixedThreadPool(4);

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
                        java.util.Locale.getDefault(),
                        "%.1f ⭐ (%d)",
                        dish.rating,
                        dish.reviewCount
                )
        );

        loadImage(
                holder.dishImage,
                dish.imageUrl
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

    private void loadImage(
            ImageView imageView,
            String imageSource
    ) {

        imageView.setTag(imageSource);
        imageView.setImageResource(placeholder);

        if (imageSource == null ||
                imageSource.trim().isEmpty()) {
            return;
        }

        String source = imageSource.trim();

        if (source.startsWith("https://") ||
                source.startsWith("http://")) {

            imageExecutor.execute(() -> {

                Bitmap bitmap = null;

                HttpURLConnection connection = null;
                InputStream input = null;

                try {

                    URL url = new URL(source);

                    connection =
                            (HttpURLConnection) url.openConnection();

                    connection.setConnectTimeout(15000);
                    connection.setReadTimeout(15000);
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty(
                            "User-Agent",
                            "Mozilla/5.0"
                    );

                    connection.connect();

                    if (connection.getResponseCode() == 200) {

                        input = connection.getInputStream();

                        bitmap = BitmapFactory.decodeStream(input);
                    }

                } catch (Exception ignored) {

                } finally {

                    try {
                        if (input != null) {
                            input.close();
                        }
                    } catch (Exception ignored) {
                    }

                    if (connection != null) {
                        connection.disconnect();
                    }
                }

                Bitmap result = bitmap;

                imageView.post(() -> {

                    if (!source.equals(imageView.getTag())) {
                        return;
                    }

                    if (result != null) {

                        imageView.setImageBitmap(result);

                    } else {

                        loadLocalDrawable(
                                imageView,
                                getImageFileName(source)
                        );
                    }
                });

            });

        } else {

            loadLocalDrawable(
                    imageView,
                    source
            );
        }
    }

    private String getImageFileName(String source) {

        try {

            String name =
                    Uri.parse(source).getLastPathSegment();

            if (name == null || name.trim().isEmpty()) {
                return "";
            }

            name = URLDecoder.decode(
                    name,
                    StandardCharsets.UTF_8.name()
            );

            return removeImageExtension(name);

        } catch (Exception e) {

            return "";
        }
    }

    private String removeImageExtension(String name) {

        if (name == null) {
            return "";
        }

        name = name.trim();

        int dot = name.lastIndexOf(".");

        if (dot > 0) {

            String extension =
                    name.substring(dot).toLowerCase();

            if (extension.equals(".jpeg") ||
                    extension.equals(".jpg") ||
                    extension.equals(".png") ||
                    extension.equals(".webp")) {

                name = name.substring(0, dot);
            }
        }

        return name;
    }

    private void loadLocalDrawable(
            ImageView imageView,
            String resourceName
    ) {

        if (resourceName == null ||
                resourceName.trim().isEmpty()) {

            imageView.setImageResource(placeholder);
            return;
        }

        String name = resourceName.trim();

        if (name.startsWith("@drawable/")) {

            name = name.substring(
                    "@drawable/".length()
            );
        }

        if (name.contains("/")) {

            name = name.substring(
                    name.lastIndexOf("/") + 1
            );
        }

        name = removeImageExtension(name);

        int resourceId = context.getResources()
                .getIdentifier(
                        name,
                        "drawable",
                        context.getPackageName()
                );

        if (resourceId != 0) {

            imageView.setImageResource(resourceId);

        } else {

            imageView.setImageResource(placeholder);
        }
    }

    @Override
    public void onViewRecycled(DishViewHolder holder) {

        super.onViewRecycled(holder);

        holder.dishImage.setTag(null);

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
