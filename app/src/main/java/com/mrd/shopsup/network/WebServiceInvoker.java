package com.mrd.shopsup.network;

import android.util.Log;

import com.mrd.shopsup.model.Temp;
import com.mrd.shopsup.model.Weather;
import com.mrd.shopsup.model.WeatherDescription;
import com.mrd.shopsup.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mayurdube on 23/03/17.
 */

public class WebServiceInvoker {

    public static String POST = "POST";
    public static String GET = "GET";

    public static final String SERVER_URL = "http://api.openweathermap.org/data/2.5/forecast/daily";

    public ArrayList<Weather> getWeatherForecast(long start, int count, int cityid, long end)
            throws IOException {

        JSONObject oJSONResponse;
        HashMap<String, String> urlParameters = new HashMap<>();
        ArrayList<Weather> weatherArrayList = null;

        urlParameters.put("APPID", Utils.WEATHERMAP_APPID);
        urlParameters.put("id", String.valueOf(cityid));
        urlParameters.put("cnt",String.valueOf(count));
        urlParameters.put("start",String.valueOf(start));
        urlParameters.put("end",String.valueOf(end));
        urlParameters.put("units","metric");
        String url = Utils.prepareURL(SERVER_URL, urlParameters);
        Log.d("web",url);


        HttpHandler httpHandler = new HttpHandler();

        oJSONResponse = httpHandler.fireHttpRequest(null, GET, url);

        if (oJSONResponse != null) {
            weatherArrayList = parseWeatherJson(oJSONResponse);
        }
        return weatherArrayList;
    }

    private ArrayList<Weather> parseWeatherJson(JSONObject oJSONResponse) {
        ArrayList<Weather> weatherArrayList = null;
        if (oJSONResponse != null) {
            if (oJSONResponse.has("list")) {
                try {
                    JSONArray arr = oJSONResponse.getJSONArray("list");
                    if (arr != null && arr.length() > 0) {
                        weatherArrayList = new ArrayList<>();
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject jsonObject = arr.getJSONObject(i);
                            Weather weather = new Weather();
                            if (jsonObject.has("dt")) {
                                weather.setDt(jsonObject.getString("dt"));
                            }

                            if (jsonObject.has("pressure")) {
                                weather.setPressure(jsonObject.getString("pressure"));
                            }

                            if (jsonObject.has("humidity")) {
                                weather.setHumidity(jsonObject.getString("humidity"));
                            }

                            if (jsonObject.has("weather")) {
                                JSONArray array = jsonObject.getJSONArray("weather");
                                if (array != null && array.length() > 0) {
                                    WeatherDescription model = new WeatherDescription();
                                    JSONObject weatherModelJson = array.getJSONObject(0);
                                    if (weatherModelJson != null && weatherModelJson.has("description")) {
                                        model.setDescription(weatherModelJson.getString("description"));
                                        WeatherDescription[] list = new WeatherDescription[1];
                                        list[0] = model;
                                        weather.setWeather(list);
                                    }
                                }
                            }

                            if (jsonObject.has("temp")) {
                                JSONObject temp = jsonObject.getJSONObject("temp");
                                if(temp != null && temp.has("max") && temp.has("min")) {
                                    Temp tempObj = new Temp();
                                    tempObj.setMax(temp.getString("max"));
                                    tempObj.setMin(temp.getString("min"));
                                    weather.setTemp(tempObj);
                                }
                            }
                            weatherArrayList.add(weather);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return weatherArrayList;
    }
}
