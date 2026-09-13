package com.iyaadam.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DishAdapter
        extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {

    private final Context context;
    private final List<Dish> dishes;
    private final OnDishClickListener listener;

    private final ExecutorService imageExecutor =
            Executors.newCachedThreadPool();

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

        View view = LayoutInflater
                .from(context)
                .inflate(
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
                dish.description
        );

        holder.prepTime.setText(
                dish.prepTime + " mins"
        );

        holder.rating.setText(
                String.format(
                        "%.1f ⭐ (%d)",
                        dish.rating,
                        dish.reviewCount
                )
        );

        /*
         * Clear old image immediately.
         */
        holder.dishImage.setImageResource(
                android.R.drawable.ic_menu_gallery
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

        if (imageSource == null ||
                imageSource.trim().isEmpty()) {

            return;
        }

        /*
         * Website image.
         */
        if (
                imageSource.startsWith("http://") ||
                imageSource.startsWith("https://")
        ) {

            imageExecutor.execute(() -> {

                Bitmap bitmap = null;

                try {

                    URL url =
                            new URL(imageSource);

                    HttpURLConnection connection =
                            (HttpURLConnection)
                                    url.openConnection();

                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);
                    connection.setDoInput(true);

                    connection.connect();

                    if (
                            connection.getResponseCode()
                                    == HttpURLConnection.HTTP_OK
                    ) {

                        InputStream input =
                                connection.getInputStream();

                        bitmap =
                                BitmapFactory.decodeStream(
                                        input
                                );

                        input.close();
                    }

                    connection.disconnect();

                } catch (Exception ignored) {
                }

                Bitmap finalBitmap = bitmap;

                imageView.post(() -> {

                    if (finalBitmap != null) {

                        imageView.setImageBitmap(
                                finalBitmap
                        );

                    } else {

                        /*
                         * Old database images such as:
                         *
                         * .../images8
                         *
                         * can still fall back to
                         * Android drawables.
                         */
                        String name =
                                imageSource.substring(
                                        imageSource.lastIndexOf("/") + 1
                                );

                        loadLocalDrawable(
                                imageView,
                                name
                        );
                    }
                });
            });

        } else {

            /*
             * Local Android drawable.
             */
            loadLocalDrawable(
                    imageView,
                    imageSource
            );
        }
    }

    private void loadLocalDrawable(
            ImageView imageView,
            String resourceName
    ) {

        try {

            int resId =
                    context.getResources()
                            .getIdentifier(
                                    resourceName,
                                    "drawable",
                                    context.getPackageName()
                            );

            if (resId != 0) {

                imageView.setImageResource(
                        resId
                );

            } else {

                imageView.setImageResource(
                        android.R.drawable.ic_menu_gallery
                );
            }

        } catch (Exception e) {

            imageView.setImageResource(
                    android.R.drawable.ic_menu_gallery
            );
        }
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

            dishImage =
                    itemView.findViewById(
                            R.id.dish_image
                    );

            dishName =
                    itemView.findViewById(
                            R.id.dish_name
                    );

            dishPrice =
                    itemView.findViewById(
                            R.id.dish_price
                    );

            dishDescription =
                    itemView.findViewById(
                            R.id.dish_description
                    );

            prepTime =
                    itemView.findViewById(
                            R.id.prep_time
                    );

            rating =
                    itemView.findViewById(
                            R.id.rating
                    );

            addToCartBtn =
                    itemView.findViewById(
                            R.id.btn_add_to_cart
                    );
        }
    }
}
