package com.example.theukuleleband.modules.dispatchman;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import android.text.TextWatcher;
import android.text.Editable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.theukuleleband.R;
import com.example.theukuleleband.DispatchReportAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DispatchReportFragment extends Fragment {
    private EditText dispatchReportEditText;
    private ImageButton dispatchReportSearchBtn;
    private ListView dispatchReportList;
    private DispatchReportAdapter adapter;
    private List<DispatchRecord> dispatchRecords = new ArrayList<>();

    private static final String API_URL = "http://your-server.com/dispatchReport.php"; // Replace with your actual API URL

    public DispatchReportFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dispatchreport, container, false);
        dispatchReportList = view.findViewById(R.id.dispatchreportlist);
        dispatchReportSearchBtn = view.findViewById(R.id.dispatchreportsearch);
        dispatchReportEditText = view.findViewById(R.id.dispatchreportedittext);

        // Initialize adapter and set it to ListView
        adapter = new DispatchReportAdapter(requireContext(), dispatchRecords);
        dispatchReportList.setAdapter(adapter);

        // Fetch dispatch data
        fetchDispatchReport();

        // Search button functionality
        dispatchReportSearchBtn.setOnClickListener(v -> filterResults(dispatchReportEditText.getText().toString().trim()));

        // Live search as user types
        dispatchReportEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterResults(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void fetchDispatchReport() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, API_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        dispatchRecords.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject obj = response.getJSONObject(i);
                                String name = obj.getString("Name");
                                String location = obj.getString("Location");
                                String dispatchDate = obj.getString("DispatchDate");

                                dispatchRecords.add(new DispatchRecord(name, location, dispatchDate));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Toast.makeText(requireContext(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(requireContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonArrayRequest);
    }

    private void filterResults(String query) {
        List<DispatchRecord> filteredList = new ArrayList<>();
        for (DispatchRecord record : dispatchRecords) {
            if (record.getName().toLowerCase().contains(query.toLowerCase()) ||
                    record.getLocation().toLowerCase().contains(query.toLowerCase()) ||
                    record.getDispatchDate().contains(query)) {
                filteredList.add(record);
            }
        }
        adapter.updateList(filteredList);
    }
}
