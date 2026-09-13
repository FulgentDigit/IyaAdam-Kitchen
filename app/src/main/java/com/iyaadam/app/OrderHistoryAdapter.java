package com.iyaadam.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter
        extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {

    public static class Order {

        public long id;
        public String orderNumber;
        public String customerName;
        public double totalAmount;
        public String status;
        public String createdAt;
        public String rawJson;
    }

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    private final Context context;
    private final List<Order> orders;
    private final OnOrderClickListener listener;

    public OrderHistoryAdapter(
            Context context,
            List<Order> orders,
            OnOrderClickListener listener
    ) {
        this.context = context;
        this.orders = orders;
        this.listener = listener;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(context).inflate(
                R.layout.order_history_item,
                parent,
                false
        );

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            OrderViewHolder holder,
            int position
    ) {

        Order order = orders.get(position);

        holder.orderNumber.setText(
                "Order #" + order.orderNumber
        );

        holder.date.setText(order.createdAt);

        holder.total.setText(
                String.format(
                        Locale.getDefault(),
                        "₦%.2f",
                        order.totalAmount
                )
        );

        holder.status.setText(
                capitalize(order.status)
        );

        holder.itemView.setOnClickListener(v -> {

            if (listener != null) {
                listener.onOrderClick(order);
            }

        });
    }

    private String capitalize(String value) {

        if (value == null || value.isEmpty()) {
            return "";
        }

        return value.substring(0, 1).toUpperCase()
                + value.substring(1);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder
            extends RecyclerView.ViewHolder {

        TextView orderNumber;
        TextView date;
        TextView total;
        TextView status;

        public OrderViewHolder(View itemView) {

            super(itemView);

            orderNumber =
                    itemView.findViewById(R.id.order_number);

            date =
                    itemView.findViewById(R.id.order_date);

            total =
                    itemView.findViewById(R.id.order_total);

            status =
                    itemView.findViewById(R.id.order_status);
        }
    }
}
