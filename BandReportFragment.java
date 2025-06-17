package com.example.theukuleleband.modules.band;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast; ////

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.theukuleleband.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BandReportFragment extends Fragment {
    private EditText bandReportEditText;
    private ImageButton bandReportSearchBtn;
    private ListView bandReportList;
    private BookingAdapter adapter;
    private List<Booking> bookingList = new ArrayList<>();
    private final String API_URL = "http://yourserver.com/bandReport.php"; // Change this to your actual URL

    public BandReportFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bandreport, container, false);

        bandReportEditText = view.findViewById(R.id.bandreportedittext);
        bandReportSearchBtn = view.findViewById(R.id.bandreportsearch);
        bandReportList = view.findViewById(R.id.bandreportlist);

        adapter = new BookingAdapter(getContext(), bookingList);
        bandReportList.setAdapter(adapter);

        // Load all records initially
        fetchBookings(API_URL);

        // Handle search button click
        bandReportSearchBtn.setOnClickListener(v -> {
            String searchQuery = bandReportEditText.getText().toString().trim();
            if (!searchQuery.isEmpty()) {
                fetchBookings(API_URL + "?search=" + searchQuery);
            } else {
                fetchBookings(API_URL); // Reload all records
            }
        });

        return view;
    }

    private void fetchBookings(String urlString) {
        new FetchBookingsTask(this).execute(urlString);
    }

    private static class FetchBookingsTask extends AsyncTask<String, Void, String> {
        private WeakReference<BandReportFragment> fragmentRef;

        FetchBookingsTask(BandReportFragment fragment) {
            fragmentRef = new WeakReference<>(fragment);
        }

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            BandReportFragment fragment = fragmentRef.get();
            if (fragment == null || !fragment.isAdded()) {
                return; // Avoid updating UI if the fragment is gone
            }

            if (result != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(result);
                    if (jsonResponse.getBoolean("success")) {
                        JSONArray bookingsArray = jsonResponse.getJSONArray("bookings");
                        fragment.bookingList.clear();
                        for (int i = 0; i < bookingsArray.length(); i++) {
                            JSONObject booking = bookingsArray.getJSONObject(i);
                            fragment.bookingList.add(new Booking(
                                    booking.getString("Genre"),
                                    booking.getString("BookingDate"),
                                    booking.getDouble("Cost"),
                                    booking.getInt("Hours")
                            ));
                        }
                        fragment.adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(fragment.getContext(), "No results found", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(fragment.getContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(fragment.getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
