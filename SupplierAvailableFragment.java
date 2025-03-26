package com.example.theukuleleband.modules.supplier;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.Filter;

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

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class SupplierAvailableFragment extends Fragment {
    private EditText suppliesEditText;
    private ImageButton suppliesSearchBtn;
    private ListView suppliesList;
    private ArrayAdapter<String> adapter;
    private List<String> supplyItems = new ArrayList<>();

    public SupplierAvailableFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.supplieravailable, container, false);
        suppliesEditText = view.findViewById(R.id.suppliesedittext);
        suppliesSearchBtn = view.findViewById(R.id.suppliessearch);
        suppliesList = view.findViewById(R.id.supplieslist);

        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, supplyItems);
        suppliesList.setAdapter(adapter);

        fetchSupplies();

        suppliesSearchBtn.setOnClickListener(v -> filterSupplies());

        return view;
    }

    private void fetchSupplies() {
        String url = "https://yourserver.com/supplierAvailable.php";
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        supplyItems.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                String supplierName = obj.getString("SupplierName");
                                String description = obj.getString("Description");
                                String brand = obj.getString("Brand");
                                supplyItems.add(supplierName + " - " + description + " - " + brand);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        queue.add(request);
    }

    private void filterSupplies() {
        String query = suppliesEditText.getText().toString().trim().toLowerCase();
        adapter.getFilter().filter(query, new Filter.FilterListener() {
            @Override
            public void onFilterComplete(int count) {
                // Optionally handle UI updates after filtering
            }
        });
    }
}
