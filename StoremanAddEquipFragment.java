package com.example.theukuleleband.modules.storeman;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class StoremanAddEquipFragment extends Fragment {
    private TextView storemanName;
    private EditText addEquipName, equipPrice;
    private Spinner addEquipDescription, addEquipBrand, addEquipCondition;
    private Button addEquipBtn;
    private RequestQueue requestQueue;
    private static final String ADD_EQUIPMENT_URL = "https://yourdomain.com/inventoryAddEquipment.php";
    private static final String STOREMAN_INFO_URL = "https://yourdomain.com/getStoremanInfo.php";

    public StoremanAddEquipFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inventoryaddequipment, container, false);

        storemanName = view.findViewById(R.id.storemanname);
        equipPrice = view.findViewById(R.id.fillamount);
        addEquipDescription = view.findViewById(R.id.newdescriptionspinner);
        addEquipBrand = view.findViewById(R.id.addequipbrandspinner);
        addEquipCondition = view.findViewById(R.id.conditionspinner);
        addEquipName = view.findViewById(R.id.newequipname);
        addEquipBtn = view.findViewById(R.id.newequipbutton);

        requestQueue = Volley.newRequestQueue(requireContext());

        fetchStoremanInfo();

        addEquipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewEquipment();
            }
        });

        return view;
    }

    private void fetchStoremanInfo() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, STOREMAN_INFO_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("success")) {
                                storemanName.setText(response.getString("Name1"));
                            } else {
                                Toast.makeText(getContext(), "Failed to fetch storeman info", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Error parsing storeman data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Failed to retrieve storeman info", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);
    }

    private void addNewEquipment() {
        String equipmentName = addEquipName.getText().toString().trim();
        String price = equipPrice.getText().toString().trim();
        String description = addEquipDescription.getSelectedItem().toString();
        String brand = addEquipBrand.getSelectedItem().toString();
        String condition = addEquipCondition.getSelectedItem().toString();

        if (equipmentName.isEmpty() || price.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            JSONObject requestData = new JSONObject();
            requestData.put("EquipmentName", equipmentName);
            requestData.put("Price", Integer.parseInt(price));
            requestData.put("Description", description);
            requestData.put("Brand", brand);
            requestData.put("Condition", condition);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ADD_EQUIPMENT_URL, requestData,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String status = response.getString("status");
                                String message = response.getString("message");
                                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                                if (status.equals("success")) {
                                    addEquipName.setText("");
                                    equipPrice.setText("");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Error parsing response", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "Request failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "JSON error", Toast.LENGTH_SHORT).show();
        }
    }
}
