package com.example.theukuleleband;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class NetworkUtils {

    private static final String TAG = "NetworkUtils";

    public static String sendPost(String requestURL, HashMap<String, String> postDataParams) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Convert HashMap data to URL encoded string
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, String> entry : postDataParams.entrySet()) {
                if (postData.length() != 0) postData.append("&");
                postData.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            Log.d(TAG, "Sending POST request to: " + requestURL);
            Log.d(TAG, "POST Data: " + postData.toString());

            // Send POST request
            OutputStream os = conn.getOutputStream();
            os.write(postData.toString().getBytes("UTF-8"));
            os.flush();
            os.close();

            // Get response code
            int responseCode = conn.getResponseCode();
            Log.d(TAG, "Response Code: " + responseCode);

            // Read response
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            Log.d(TAG, "Response: " + response.toString());

        } catch (Exception e) {
            Log.e(TAG, "Error in sendPost: " + e.getMessage(), e);
            return "{\"success\":false,\"message\":\"Network error: " + e.getMessage() + "\"}";
        }
        return response.toString();
    }
}
