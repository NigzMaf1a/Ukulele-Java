package com.example.theukuleleband.modules.dispatchman;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.theukuleleband.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DispatchDispatchFragment extends Fragment {
    private RecyclerView dispatchList;
    private DispatchAdapter adapter;
    private ArrayList<DispatchItem> dispatchItems;
    private static final String FETCH_URL = "https://yourserver.com/api/dispatchDispatch.php";
    private static final String UPDATE_URL = "https://yourserver.com/api/updateDispatch.php";

    public DispatchDispatchFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dispatchdispatch, container, false);
        dispatchList = view.findViewById(R.id.dispatchlist);
        dispatchList.setLayoutManager(new LinearLayoutManager(getContext()));
        dispatchItems = new ArrayList<>();
        adapter = new DispatchAdapter(dispatchItems);
        dispatchList.setAdapter(adapter);
        fetchDispatchData();
        return view;
    }

    private void fetchDispatchData() {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, FETCH_URL, null,
                response -> {
                    try {
                        if (response.getString("status").equals("success")) {
                            JSONArray dataArray = response.getJSONArray("data");
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject obj = dataArray.getJSONObject(i);
                                dispatchItems.add(new DispatchItem(
                                        obj.getString("Name"),
                                        obj.getString("PhoneNo"),
                                        obj.getString("Location")
                                ));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                Throwable::printStackTrace);
        queue.add(request);
    }

    private void updateDispatchStatus(DispatchItem item) {
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest request = new StringRequest(Request.Method.POST, UPDATE_URL,
                response -> {
                    Toast.makeText(getContext(), "Dispatch updated", Toast.LENGTH_SHORT).show();
                    dispatchItems.remove(item);
                    adapter.notifyDataSetChanged();
                },
                error -> Toast.makeText(getContext(), "Error updating dispatch", Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", item.getName());
                return params;
            }
        };
        queue.add(request);
    }

    private class DispatchAdapter extends RecyclerView.Adapter<DispatchAdapter.ViewHolder> {
        private ArrayList<DispatchItem> itemList;

        public DispatchAdapter(ArrayList<DispatchItem> itemList) {
            this.itemList = itemList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dispatch_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            DispatchItem item = itemList.get(position);
            holder.name.setText(item.getName());
            holder.phone.setText(item.getPhone());
            holder.location.setText(item.getLocation());
            holder.dispatchButton.setOnClickListener(v -> updateDispatchStatus(item));
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, phone, location;
            Button dispatchButton;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                phone = itemView.findViewById(R.id.phone);
                location = itemView.findViewById(R.id.location);
                dispatchButton = itemView.findViewById(R.id.dispatch_button);
            }
        }
    }

    private class DispatchItem {
        private final String name, phone, location;

        public DispatchItem(String name, String phone, String location) {
            this.name = name;
            this.phone = phone;
            this.location = location;
        }

        public String getName() { return name; }
        public String getPhone() { return phone; }
        public String getLocation() { return location; }
    }
}