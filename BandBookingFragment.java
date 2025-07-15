package com.example.theukuleleband.modules.band;
////////////////

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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.theukuleleband.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class BandBookingFragment extends Fragment {
    private RecyclerView bandBookingsRecycler;
    private BandBookingAdapter adapter;
    private List<BandBookingModel> bookings;
    private RequestQueue requestQueue;

    public BandBookingFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bandbookings, container, false);

        // Initialize RecyclerView
        bandBookingsRecycler = view.findViewById(R.id.bandbookings_recycler);
        bandBookingsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize data list & adapter
        bookings = new ArrayList<>();
        adapter = new BandBookingAdapter(getContext(), bookings);
        bandBookingsRecycler.setAdapter(adapter);

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(requireContext());

        // Fetch data from API
        fetchBookings();

        return view;
    }

    private void fetchBookings() {
        String url = "https://yourdomain.com/api/bandBooking.php";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray data = response.getJSONArray("data");
                        bookings.clear(); // Clear old data before adding new
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);
                            bookings.add(new BandBookingModel(
                                    obj.getInt("BookingID"),
                                    obj.getString("Genre"),
                                    obj.getString("BookingDate"),
                                    obj.getString("BookStatus")
                            ));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing data!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Error fetching data!", Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(request);
    }
}
