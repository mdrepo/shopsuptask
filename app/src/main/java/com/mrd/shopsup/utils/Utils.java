package com.mrd.shopsup.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by mayurdube on 23/03/17.
 */

public class Utils {

    public static final String WEATHERMAP_APPID = "09d88d3c126e9ae9ddc913deb48aa313";
    public static final int WEATHERMAP_BANGLORE_ID = 1277333;
    public static final int WEATHERMAP_DELHI_ID = 1273294;
    public static final int WEATHERMAP_CHENNAI_ID = 1264527;


    public static String prepareURL(String url, HashMap<String,String> keyValuePair) {
        if(!TextUtils.isEmpty(url) && keyValuePair != null && keyValuePair.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder(url);
            if(!url.contains("?")) {
                stringBuilder.append("?");
            }

            Iterator<Map.Entry<String, String>> iterator = keyValuePair.entrySet().iterator();
            while(iterator.hasNext()) {
                Map.Entry<String,String> entry = iterator.next();
                stringBuilder.append(entry.getKey());
                stringBuilder.append("=");
                stringBuilder.append(entry.getValue());
                stringBuilder.append("&");
            }
            stringBuilder = stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("&"));
            url = stringBuilder.toString();
        }
        return url;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
