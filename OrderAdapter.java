package com.example.theukuleleband.modules.storeman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.theukuleleband.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;
    private List<OrderItem> orderList;
    private OnOrderClickListener orderClickListener;

    public interface OnOrderClickListener {
        void onOrderClick(OrderItem orderItem);
    }

    public OrderAdapter(Context context, List<OrderItem> orderList, OnOrderClickListener orderClickListener) {
        this.context = context;
        this.orderList = orderList;
        this.orderClickListener = orderClickListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderItem item = orderList.get(position);
        holder.supplyName.setText(item.getSupplierName());
        holder.description.setText(item.getDescription());
        holder.brand.setText(item.getBrand());

        holder.orderButton.setOnClickListener(v -> {
            if (orderClickListener != null) {
                orderClickListener.onOrderClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView supplyName, description, brand;
        Button orderButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            supplyName = itemView.findViewById(R.id.supply_name);
            description = itemView.findViewById(R.id.description);
            brand = itemView.findViewById(R.id.brand);
            orderButton = itemView.findViewById(R.id.order_button);
        }
    }
}
