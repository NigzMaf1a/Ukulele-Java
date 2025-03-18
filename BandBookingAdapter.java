package com.example.theukuleleband.modules.band;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.theukuleleband.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class BandBookingAdapter extends RecyclerView.Adapter<BandBookingAdapter.ViewHolder> {

    private List<BandBookingModel> bookings;
    private Context context;

    public BandBookingAdapter(Context context, List<BandBookingModel> bookings) {
        this.context = context;
        this.bookings = bookings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_band_booking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BandBookingModel booking = bookings.get(position);

        holder.tvGenre.setText("Genre: " + booking.getGenre());
        holder.tvBookingDate.setText("Date: " + booking.getBookingDate());
        holder.tvBookStatus.setText("Status: " + booking.getBookStatus());

        // Button click to update status
        holder.btnChangeStatus.setOnClickListener(v -> {
            updateBookStatus(booking.getBookingID(), position);
        });
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvGenre, tvBookingDate, tvBookStatus;
        Button btnChangeStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            tvGenre = itemView.findViewById(R.id.tv_genre);
            tvBookingDate = itemView.findViewById(R.id.tv_booking_date);
            tvBookStatus = itemView.findViewById(R.id.tv_book_status);
            btnChangeStatus = itemView.findViewById(R.id.btn_change_status);
        }
    }

    private void updateBookStatus(int bookingID, int position) {
        String url = "https://yourdomain.com/api/bandBooking.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equals("success")) {
                            bookings.remove(position); // Remove item from list
                            notifyItemRemoved(position); // Notify RecyclerView
                            Toast.makeText(context, "Book status updated!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to update!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(context, "Network error!", Toast.LENGTH_SHORT).show()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("BookingID", String.valueOf(bookingID));
                params.put("NewStatus", "Tick"); // Change status
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }
}
