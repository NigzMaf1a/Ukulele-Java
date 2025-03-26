package com.example.theukuleleband.modules.storeman;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.theukuleleband.R;
import com.example.theukuleleband.modules.storeman.LendingItem;
import java.util.List;

public class LendingAdapter extends RecyclerView.Adapter<LendingAdapter.ViewHolder> {
    private List<LendingItem> lendingList;
    private OnApproveClickListener onApproveClickListener;

    public interface OnApproveClickListener {
        void onApprove(int lendID);
    }

    public LendingAdapter(List<LendingItem> lendingList, OnApproveClickListener listener) {
        this.lendingList = lendingList;
        this.onApproveClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lending, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LendingItem item = lendingList.get(position);
        holder.genreText.setText(item.getGenre());
        holder.lendTypeText.setText(item.getLendingType());
        holder.costText.setText("Cost: " + item.getCost());
        holder.hoursText.setText("Hours: " + item.getHours());

        holder.approveButton.setOnClickListener(v -> {
            onApproveClickListener.onApprove(item.getLendID());
        });
    }

    @Override
    public int getItemCount() {
        return lendingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView genreText, lendTypeText, costText, hoursText;
        Button approveButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            genreText = itemView.findViewById(R.id.genre_text);
            lendTypeText = itemView.findViewById(R.id.lend_type_text);
            costText = itemView.findViewById(R.id.cost_text);
            hoursText = itemView.findViewById(R.id.hours_text);
            approveButton = itemView.findViewById(R.id.approve_button);
        }
    }
}
