package com.example.theukuleleband.modules.supplier;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.theukuleleband.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SupplierReportFragment extends Fragment {
    private EditText supplierReportEditText;
    private ImageButton supplierReportSearchBtn;
    private ListView supplierReportList;
    private ArrayAdapter<String> adapter;
    private List<String> supplierReportData;
    private RequestQueue requestQueue;

    private static final String URL = "https://yourserver.com/supplierReport.php"; // Change to your actual endpoint

    public SupplierReportFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.supplierreport, container, false);
        supplierReportEditText = view.findViewById(R.id.supplierreportedittext);
        supplierReportList = view.findViewById(R.id.supplierreportlist);
        supplierReportSearchBtn = view.findViewById(R.id.supplierreportsearch);

        requestQueue = Volley.newRequestQueue(requireContext());
        supplierReportData = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, supplierReportData);
        supplierReportList.setAdapter(adapter);

        loadSupplierReport("");

        supplierReportSearchBtn.setOnClickListener(v -> {
            String query = supplierReportEditText.getText().toString().trim();
            loadSupplierReport(query);
        });

        return view;
    }

    private void loadSupplierReport(String searchQuery) {
        String fullUrl = searchQuery.isEmpty() ? URL : URL + "?search=" + searchQuery;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, fullUrl, null,
                response -> {
                    supplierReportData.clear();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject item = response.getJSONObject(i);
                            String description = item.getString("Description");
                            String brand = item.getString("Brand");
                            String supplyDate = item.getString("SupplyDate");
                            supplierReportData.add(description + " - " + brand + " - " + supplyDate);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "JSON Parsing Error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(requireContext(), "Error Fetching Data", Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(jsonArrayRequest);
    }
}
