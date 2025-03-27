package com.example.theukuleleband.modules.storeman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.theukuleleband.R;
import java.util.List;

public class StoremanEquipAdapter extends RecyclerView.Adapter<StoremanEquipAdapter.ViewHolder> {

    private List<SupplyItem> supplyList;
    private Context context;
    private static final String ORDER_URL = "http://yourserver.com/inventoryAddOrder.php"; // Change to actual URL

    public StoremanEquipAdapter(List<SupplyItem> supplyList, Context context) {
        this.supplyList = supplyList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_supply_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SupplyItem item = supplyList.get(position);
        holder.supplyName.setText(item.getSupplyName());
        holder.description.setText(item.getDescription());
        holder.brand.setText(item.getBrand());

        holder.orderButton.setOnClickListener(v -> {
            StringRequest request = new StringRequest(Request.Method.POST, ORDER_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(context, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Order failed!", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected java.util.Map<String, String> getParams() {
                    java.util.Map<String, String> params = new java.util.HashMap<>();
                    params.put("SupplyName", item.getSupplyName());
                    params.put("Description", item.getDescription());
                    params.put("Brand", item.getBrand());
                    return params;
                }
            };

            Volley.newRequestQueue(context).add(request);
        });
    }

    @Override
    public int getItemCount() { return supplyList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView supplyName, description, brand;
        Button orderButton;

        public ViewHolder(View itemView) {
            super(itemView);
            supplyName = itemView.findViewById(R.id.supplyName);
            description = itemView.findViewById(R.id.description);
            brand = itemView.findViewById(R.id.brand);
            orderButton = itemView.findViewById(R.id.orderButton);
        }
    }
}
