package com.example.theukuleleband.modules.mcee;

import android.os.Bundle;
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

import org.json.JSONObject;

public class MceeDashboardFragment extends Fragment {
    private TextView nextBookLocation, nextBookGenre, nextCustomer, nextCustomerPhone, completedBookings;
    private RequestQueue requestQueue;

    public MceeDashboardFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboardmcee, container, false);
        nextBookLocation = view.findViewById(R.id.mceedashnextbooklocation);
        nextBookGenre = view.findViewById(R.id.mceedashnextbookgenre);
        nextCustomer = view.findViewById(R.id.mceedashnextbookcustomer);
        nextCustomerPhone = view.findViewById(R.id.mceedashnextcustomerphone);
        completedBookings = view.findViewById(R.id.mceedashcompletedbookings);

        requestQueue = Volley.newRequestQueue(requireContext());
        fetchDashboardData();

        return view;
    }

    private void fetchDashboardData() {
        String url = "https://yourserver.com/mceeDashboard.php";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("success")) {
                                JSONObject data = response.getJSONObject("data");
                                nextBookLocation.setText(data.getString("dLocation"));
                                nextBookGenre.setText(data.getString("Genre"));
                                nextCustomer.setText(data.getString("Name1"));
                                nextCustomerPhone.setText(data.getString("PhoneNo"));
                                completedBookings.setText(String.valueOf(data.getInt("TotalDoneLending")));
                            } else {
                                Toast.makeText(getContext(), "Error loading data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Data parsing error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
}