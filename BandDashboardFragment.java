package com.example.theukuleleband.modules.band;

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

public class BandDashboardFragment extends Fragment {
    private TextView nextBookLocation, nextPerfomanceGenre, nextBookingCharges, newCommsSender, pendBookTotal;
    private static final String DASHBOARD_URL = "https://yourserver.com/bandDashboard.php";
    private RequestQueue requestQueue;

    public BandDashboardFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboardband, container, false);
        nextBookLocation = view.findViewById(R.id.bandnextlocation);
        nextPerfomanceGenre = view.findViewById(R.id.nextperfomancegenre);
        nextBookingCharges = view.findViewById(R.id.bandnextbookcharges);
        newCommsSender = view.findViewById(R.id.bandnewcomms);
        pendBookTotal = view.findViewById(R.id.bandpendbook);

        requestQueue = Volley.newRequestQueue(requireContext());
        fetchDashboardData();

        return view;
    }

    private void fetchDashboardData() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, DASHBOARD_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                nextBookLocation.setText(response.getString("location"));
                                nextPerfomanceGenre.setText(response.getString("genre"));
                                nextBookingCharges.setText(String.valueOf(response.getInt("cost")));
                                newCommsSender.setText(response.getString("latest_comms_sender"));
                                pendBookTotal.setText(String.valueOf(response.getInt("untick_bookings_count")));
                            } else {
                                Toast.makeText(getContext(), "Failed to load dashboard data", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error parsing dashboard data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error fetching dashboard data", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
}
