package com.example.theukuleleband.modules.storeman;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.theukuleleband.R;
import com.example.theukuleleband.modules.storeman.OrderAdapter;
import com.example.theukuleleband.modules.storeman.OrderItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoremanApproveDeliveryFragment extends Fragment {
    private RecyclerView approveDeliList;
    private OrderAdapter orderAdapter;
    private List<OrderItem> orderList;
    private static final String FETCH_URL = "https://yourdomain.com/inventoryDeliveryApprove.php";
    private static final String UPDATE_URL = "https://yourdomain.com/updateOrderStatus.php";

    public StoremanApproveDeliveryFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inventoryorderdeli, container, false);
        approveDeliList = view.findViewById(R.id.inventoryorderdelilist);
        approveDeliList.setLayoutManager(new LinearLayoutManager(getContext()));

        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(getContext(), orderList, this::approveOrder);
        approveDeliList.setAdapter(orderAdapter);

        fetchOrders();
        return view;
    }

    private void fetchOrders() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, FETCH_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        orderList.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                String supplier = obj.getString("SupplierName");
                                String description = obj.getString("Description");
                                String brand = obj.getString("Brand");

                                orderList.add(new OrderItem(supplier, description, brand));
                            }
                            orderAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, error -> Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show()
        );

        Volley.newRequestQueue(getContext()).add(request);
    }

    private void approveOrder(OrderItem item) {
        StringRequest request = new StringRequest(Request.Method.POST, UPDATE_URL,
                response -> {
                    if (response.equals("success")) {
                        orderList.remove(item);
                        orderAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Order Approved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Approval Failed", Toast.LENGTH_SHORT).show();
                    }
                }, error -> Toast.makeText(getContext(), "Request Failed", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("SupplierName", item.getSupplierName());
                params.put("Description", item.getDescription());
                params.put("Brand", item.getBrand());
                params.put("OrderStatus", "Delivered");
                return params;
            }
        };

        Volley.newRequestQueue(getContext()).add(request);
    }
}
