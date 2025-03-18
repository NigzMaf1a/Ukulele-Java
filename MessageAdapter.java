package com.example.theukuleleband.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.theukuleleband.R;
import com.example.theukuleleband.models.Message;
import com.example.theukuleleband.utils.VolleySingleton;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private Context context;
    private List<Message> messageList;
    private static final String UPDATE_MESSAGE_URL = "https://yourdomain.com/updateMessageResponse.php";

    public MessageAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_item, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);

        holder.senderName.setText(message.getSenderName());
        holder.receiverName.setText(message.getReceiverName());
        holder.messageBody.setText(message.getMessageBody());

        if (message.getMessageResponse().isEmpty()) {
            holder.messageResponse.setVisibility(View.GONE);
        } else {
            holder.messageResponse.setText(message.getMessageResponse());
            holder.messageResponse.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (message.getMessageResponse().isEmpty()) {
                holder.responseLayout.setVisibility(View.VISIBLE);
            }
        });

        holder.sendResponseButton.setOnClickListener(v -> {
            String responseText = holder.responseEditText.getText().toString().trim();
            if (!responseText.isEmpty()) {
                sendResponse(message.getMessageId(), responseText, holder);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView senderName, receiverName, messageBody, messageResponse;
        EditText responseEditText;
        Button sendResponseButton;
        View responseLayout;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderName = itemView.findViewById(R.id.senderName);
            receiverName = itemView.findViewById(R.id.receiverName);
            messageBody = itemView.findViewById(R.id.messageBody);
            messageResponse = itemView.findViewById(R.id.messageResponse);
            responseEditText = itemView.findViewById(R.id.responseEditText);
            sendResponseButton = itemView.findViewById(R.id.sendResponseButton);
            responseLayout = itemView.findViewById(R.id.responseLayout);
            responseLayout.setVisibility(View.GONE);
        }
    }

    private void sendResponse(int messageId, String response, MessageViewHolder holder) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPDATE_MESSAGE_URL,
                response1 -> {
                    holder.messageResponse.setText(response);
                    holder.messageResponse.setVisibility(View.VISIBLE);
                    holder.responseLayout.setVisibility(View.GONE);
                },
                error -> error.printStackTrace()) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("messageId", String.valueOf(messageId));
                params.put("response", response);
                return params;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}
