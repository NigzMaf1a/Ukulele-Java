package com.example.theukuleleband.modules.supplier;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.theukuleleband.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SupplierAddSupplyFragment extends Fragment {

    private Spinner descriptionSpinner, brandSpinner;
    private EditText priceEditText, supplyNameEditText;
    private Button chooseImageButton, addSupplyButton;
    private ImageView imageView;

    private Bitmap selectedBitmap; // To store selected image
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String UPLOAD_URL = "https://yourserver.com/supplierAddSupply.php"; // Change this to your actual PHP endpoint

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.supplieraddsupply, container, false);

        // Initialize UI components
        descriptionSpinner = view.findViewById(R.id.addsupplydescription);
        brandSpinner = view.findViewById(R.id.addsupplybrand);
        priceEditText = view.findViewById(R.id.priceedittext);
        supplyNameEditText = view.findViewById(R.id.jinayasupply);
        chooseImageButton = view.findViewById(R.id.addsupplychooseimage);
        addSupplyButton = view.findViewById(R.id.addsupplybutton);
        imageView = view.findViewById(R.id.supplyimage);

        // Populate spinners
        setupSpinners();

        // Handle image selection
        chooseImageButton.setOnClickListener(v -> pickImageFromGallery());

        // Handle supply addition
        addSupplyButton.setOnClickListener(v -> uploadSupplyData());

        return view;
    }

    private void setupSpinners() {
        // Populate Description Spinner
        ArrayAdapter<CharSequence> descriptionAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.description_array, // Ensure this array is defined in res/values/strings.xml
                android.R.layout.simple_spinner_item
        );
        descriptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        descriptionSpinner.setAdapter(descriptionAdapter);

        // Populate Brand Spinner
        ArrayAdapter<CharSequence> brandAdapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.brand_array, // Ensure this array is defined in res/values/strings.xml
                android.R.layout.simple_spinner_item
        );
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandSpinner.setAdapter(brandAdapter);
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                imageView.setImageBitmap(selectedBitmap); // Display image
            } catch (IOException e) {
                Toast.makeText(getContext(), "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadSupplyData() {
        String price = priceEditText.getText().toString().trim();
        String supplyName = supplyNameEditText.getText().toString().trim();
        String description = descriptionSpinner.getSelectedItem().toString();
        String brand = brandSpinner.getSelectedItem().toString();

        // Validate input
        if (price.isEmpty() || supplyName.isEmpty() || selectedBitmap == null) {
            Toast.makeText(getContext(), "Fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert image to Base64
        String imageBase64 = encodeImageToBase64(selectedBitmap);

        // Send data via Volley
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                response -> {
                    Toast.makeText(getContext(), "Supply added successfully", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Toast.makeText(getContext(), "Failed to add supply: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Price", price);
                params.put("SupplyName", supplyName);
                params.put("Description", description);
                params.put("Brand", brand);
                params.put("ImageURL", imageBase64);
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
