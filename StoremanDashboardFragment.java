package com.example.theukuleleband.modules.storeman;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class StoremanDashboardFragment extends Fragment {
    private TextView nextOrderBrand, hiredEquipment, deliveredEquipment, availableEquipment, damagedEquipment;

    public StoremanDashboardFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboardinventory, container, false);

        // Initialize TextViews
        nextOrderBrand = view.findViewById(R.id.inventorydashnextorderbrand);
        hiredEquipment = view.findViewById(R.id.inventorydashhiredequipment);
        deliveredEquipment = view.findViewById(R.id.inventorydashinasumua);
        availableEquipment = view.findViewById(R.id.inventorydashavailableequip);
        damagedEquipment = view.findViewById(R.id.inventorydashdamageequip);

        // Load dashboard data from server
        loadDashboardData();

        return view;
    }

    // Method to load data from inventoryDashboard.php using Volley
    private void loadDashboardData() {
        String url = "http://your-server-address/inventoryDashboard.php";  // Replace with your actual URL
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        // Create a JSON request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Extract data from the response JSON
                            String latestOrderBrand = response.getString("latest_order_brand");
                            int totalUnavailable = response.getInt("total_unavailable_inventory");
                            int totalDelivered = response.getInt("total_delivered_orders");
                            int totalAvailable = response.getInt("total_available_inventory");
                            int totalNotCAT1 = response.getInt("total_not_CAT1");

                            // Update the UI with the fetched data
                            nextOrderBrand.setText(latestOrderBrand);
                            hiredEquipment.setText(String.valueOf(totalUnavailable)); // Assuming hired equipment equals unavailable
                            deliveredEquipment.setText(String.valueOf(totalDelivered));
                            availableEquipment.setText(String.valueOf(totalAvailable));
                            damagedEquipment.setText(String.valueOf(totalNotCAT1)); // Assuming damaged equipment equals not CAT1

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyError", error.toString());
                        Toast.makeText(getContext(), "Error loading data", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the request queue
        requestQueue.add(jsonObjectRequest);
    }
}
