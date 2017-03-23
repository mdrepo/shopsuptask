package com.mrd.shopsup.controllers;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import com.mrd.shopsup.model.Weather;
import com.mrd.shopsup.network.WebServiceInvoker;
import com.mrd.shopsup.utils.Utils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by mayurdube on 23/03/17.
 */

public class WeatherController extends Controller {

    public static final int GET_WEATHER = 2001;

    HandlerThread workerThread;
    Handler workerHandler;
    Context mContext;

    public WeatherController(Context context) {
        mContext = context;
        workerThread = new HandlerThread("ReviewControllerThread");
        workerThread.start();
        workerHandler = new Handler(workerThread.getLooper());
    }

    @Override
    public boolean handleMessage(int what, Object data) {
        switch (what) {
            case GET_WEATHER:
                getWeather((int)data);
                break;
        }
        return true;
    }

    private void getWeather(final int data) {
        workerHandler.post(new Runnable() {
            @Override
            public void run() {
                if(Utils.isNetworkAvailable(mContext)) {
                    long start = System.currentTimeMillis();
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_MONTH,7);
                    long end = cal.getTimeInMillis();
                    WebServiceInvoker invoker = new WebServiceInvoker();
                    try {
                        ArrayList<Weather> reposne = invoker.getWeatherForecast(start,7,data,end);
                        if(reposne != null) {
                            notifyOutboxHandlers(GET_WEATHER,data,0,reposne);
                        } else {
                            notifyOutboxHandlers(MESSAGE_NO_DATA,data,0,reposne);
                        }
                    } catch (SocketTimeoutException e) {
                        e.printStackTrace();
                        notifyOutboxHandlers(MESSAGE_NO_NETWORK,data,0,"Couldn't connect to server. Please check internet");
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                        notifyOutboxHandlers(MESSAGE_NO_NETWORK,data,0,"Couldn't connect to server");
                    } catch (IOException e) {
                        e.printStackTrace();
                        notifyOutboxHandlers(MESSAGE_NO_NETWORK,data,0,"Couldn't connect to server");
                    }
                } else {
                   notifyOutboxHandlers(MESSAGE_NO_NETWORK,data,0,"Please check internet connection");
                }
            }
        });
    }
}
