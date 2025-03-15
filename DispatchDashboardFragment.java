package com.example.theukuleleband.modules.dispatchman;

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

import org.json.JSONException;
import org.json.JSONObject;

public class DispatchDashboardFragment extends Fragment {
    private TextView nextDispatchLocation, nextCustomerName, nextCustomerPhone, nextDispatchDate, completedDispatches;
    private static final String URL = "https://yourserver.com/dispatchDashboard.php"; // Replace with actual URL

    public DispatchDashboardFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboarddispatch, container, false);

        nextDispatchLocation = view.findViewById(R.id.dispatchnextlocation);
        nextCustomerName = view.findViewById(R.id.dispatchnextcustomername);
        nextCustomerPhone = view.findViewById(R.id.dispatchnextcustomerphone);
        nextDispatchDate = view.findViewById(R.id.dispatchnextdate);
        completedDispatches = view.findViewById(R.id.dispatchcompleted);

        fetchDispatchData();

        return view;
    }

    private void fetchDispatchData() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("success")) {
                                nextDispatchLocation.setText(response.getString("location"));
                                nextCustomerName.setText(response.getString("name"));
                                nextCustomerPhone.setText(response.getString("phone"));
                                nextDispatchDate.setText(response.getString("dispatchDate"));
                                completedDispatches.setText(String.valueOf(response.getInt("totalDispatched")));
                            } else {
                                Toast.makeText(requireContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(requireContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(request);
    }
}
