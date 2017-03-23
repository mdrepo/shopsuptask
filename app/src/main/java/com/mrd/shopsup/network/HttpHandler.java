package com.mrd.shopsup.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;


/**
 * Created by mayurdube on 23/03/17.
 */

import javax.net.ssl.HttpsURLConnection;

public class HttpHandler {

    public JSONObject fireHttpRequest(JSONObject p_JSONObject, String p_requestMethod, String p_path)
            throws IOException {

        HttpURLConnection connection = null;
        String result = null;
        JSONObject JSONResult = null;
        StringBuilder builder = new StringBuilder();

        try {
            URL url = new URL(p_path);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(3000);
            connection.setConnectTimeout(3000);
            if (p_requestMethod.equalsIgnoreCase("POST")) {
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.writeBytes(p_JSONObject.toString());
                wr.flush();
                wr.close();
            } else {
                connection.setRequestMethod("GET");
            }
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                InputStream oInputStream = connection.getInputStream();
                BufferedReader oBufferedReader = new BufferedReader(new InputStreamReader(oInputStream));

                String line;
                while ((line = oBufferedReader.readLine()) != null) {
                    builder.append(line);
                }
                JSONResult = new JSONObject(builder.toString());
            } else {
                InputStream oInputStream = connection.getErrorStream();
                BufferedReader oBufferedReader = new BufferedReader(new InputStreamReader(oInputStream));

                String line;
                while ((line = oBufferedReader.readLine()) != null) {
                    builder.append(line);
                }

                Log.d("http", "Status code : " + responseCode + " " + builder.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return JSONResult;
    }
}
