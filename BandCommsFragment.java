package com.example.theukuleleband.modules.band;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.theukuleleband.R;
import com.example.theukuleleband.adapters.MessageAdapter;
import com.example.theukuleleband.models.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BandCommsFragment extends Fragment {
    private EditText bandCommsEditText;
    private Spinner bandCommsRecipient;
    private ImageButton bandCommsSendBtn;
    private RecyclerView bandCommsList;
    private MessageAdapter messageAdapter;
    private ArrayList<Message> messageList;
    private RequestQueue requestQueue;
    private Context context;

    private static final String FETCH_RECIPIENTS_URL = "https://yourserver.com/crewComms.php";
    private static final String SEND_MESSAGE_URL = "https://yourserver.com/crewComms.php";
    private static final String FETCH_MESSAGES_URL = "https://yourserver.com/crewComms.php";
    private static final String UPDATE_RESPONSE_URL = "https://yourserver.com/updateMessageResponse.php";

    public BandCommsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bandcomms, container, false);
        context = getContext();
        requestQueue = Volley.newRequestQueue(context);

        bandCommsEditText = view.findViewById(R.id.bandcommstext);
        bandCommsRecipient = view.findViewById(R.id.bandcommsrecipient);
        bandCommsSendBtn = view.findViewById(R.id.bandcommsbtn);
        bandCommsList = view.findViewById(R.id.bandcommslist);

        bandCommsList.setLayoutManager(new LinearLayoutManager(context));
        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(context, messageList);
        bandCommsList.setAdapter(messageAdapter);

        fetchRecipients();
        fetchMessages();

        bandCommsSendBtn.setOnClickListener(v -> sendMessage());

        return view;
    }

    private void fetchRecipients() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, FETCH_RECIPIENTS_URL, null,
                response -> {
                    ArrayList<String> recipients = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            recipients.add(obj.getString("name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, recipients);
                    bandCommsRecipient.setAdapter(adapter);
                },
                Throwable::printStackTrace);

        requestQueue.add(request);
    }

    private void sendMessage() {
        String message = bandCommsEditText.getText().toString().trim();
        String recipient = bandCommsRecipient.getSelectedItem().toString();

        if (message.isEmpty()) {
            Toast.makeText(context, "Enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest request = new StringRequest(Request.Method.POST, SEND_MESSAGE_URL,
                response -> {
                    Toast.makeText(context, "Message Sent!", Toast.LENGTH_SHORT).show();
                    fetchMessages();
                },
                Throwable::printStackTrace) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("message", message);
                params.put("recipient", recipient);
                return params;
            }
        };

        requestQueue.add(request);
    }

    private void fetchMessages() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, FETCH_MESSAGES_URL, null,
                response -> {
                    messageList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            messageList.add(new Message(
                                    obj.getString("sender"),
                                    obj.getString("receiver"),
                                    obj.getString("message"),
                                    obj.optString("response", "")
                            ));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    messageAdapter.notifyDataSetChanged();
                },
                Throwable::printStackTrace);

        requestQueue.add(request);
    }
}
