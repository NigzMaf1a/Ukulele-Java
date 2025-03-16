package com.example.theukuleleband.modules.supplier;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.theukuleleband.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SupplierDashboardFragment extends Fragment {
    private TextView pendingOrders, nextOrderAmount, nextOrderType, nextOrderBrand, deliveredOrders;
    private RequestQueue requestQueue;

    public SupplierDashboardFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboardsupplier, container, false);

        pendingOrders = view.findViewById(R.id.supdashpendorder);
        nextOrderAmount = view.findViewById(R.id.supdashnextordercash);
        nextOrderType = view.findViewById(R.id.supdashnextordertype);
        nextOrderBrand = view.findViewById(R.id.supdashnextorderbrand);
        deliveredOrders = view.findViewById(R.id.supdashdelivered);

        requestQueue = Volley.newRequestQueue(requireContext());
        loadDashboardData();

        return view;
    }

    private void loadDashboardData() {
        String url = "https://yourserver.com/supplierDashboard.php"; // Replace with actual endpoint URL

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            pendingOrders.setText(String.valueOf(response.getInt("undelivered_count")));
                            nextOrderAmount.setText(String.valueOf(response.getInt("latest_order_price")));
                            nextOrderType.setText(response.getString("latest_order_description"));
                            nextOrderBrand.setText(response.getString("latest_order_brand"));
                            deliveredOrders.setText(String.valueOf(response.getInt("delivered_count")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }
}
