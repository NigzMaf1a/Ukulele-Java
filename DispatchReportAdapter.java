package com.example.theukuleleband;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.BaseAdapter;

import com.example.theukuleleband.R;
import com.example.theukuleleband.modules.dispatchman.DispatchRecord;

import java.util.List;

public class DispatchReportAdapter extends BaseAdapter {
    private Context context;
    private List<DispatchRecord> dispatchList;

    public DispatchReportAdapter(Context context, List<DispatchRecord> dispatchList) {
        this.context = context;
        this.dispatchList = dispatchList;
    }

    @Override
    public int getCount() {
        return dispatchList.size();
    }

    @Override
    public Object getItem(int position) {
        return dispatchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dispatch_report_item, parent, false);
        }

        DispatchRecord record = dispatchList.get(position);

        TextView nameTextView = convertView.findViewById(R.id.dispatch_name);
        TextView locationTextView = convertView.findViewById(R.id.dispatch_location);
        TextView dateTextView = convertView.findViewById(R.id.dispatch_date);

        nameTextView.setText(record.getName());
        locationTextView.setText(record.getLocation());
        dateTextView.setText(record.getDispatchDate());

        return convertView;
    }

    public void updateList(List<DispatchRecord> newList) {
        dispatchList.clear();
        dispatchList.addAll(newList);
        notifyDataSetChanged();
    }
}
