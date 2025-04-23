package com.example.theukuleleband.modules.accountant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class AccountantDashboardFragment extends Fragment {
    private TextView newOrderAmount, latestPayment, lastWithdrawAmount, lastTransactionType, accountBalance;
    private static final String FINANCE_DASHBOARD_URL = "https://yourserver.com/financeDashboard.php";

    public AccountantDashboardFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboardfinance, container, false);

        newOrderAmount = view.findViewById(R.id.financedashorder);
        latestPayment = view.findViewById(R.id.financedashlatestpay);
        lastWithdrawAmount = view.findViewById(R.id.financedashlastwithdraw);
        lastTransactionType = view.findViewById(R.id.financedashlasttransactiontype);
        accountBalance = view.findViewById(R.id.financedashbalance);

        fetchFinanceDashboardData();

        return view;
    }

    private void fetchFinanceDashboardData() {
        //Fetches data for the accountant module
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                FINANCE_DASHBOARD_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            newOrderAmount.setText(String.valueOf(response.optInt("latest_order_price", 0)));
                            latestPayment.setText(String.valueOf(response.optInt("latest_customer_payment_amount", 0)));
                            lastWithdrawAmount.setText(String.valueOf(response.optInt("latest_withdraw_amount", 0)));
                            lastTransactionType.setText(response.optString("latest_transaction_type", "N/A"));
                            accountBalance.setText(String.valueOf(response.optInt("total_customer_payments", 0)));
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
}
