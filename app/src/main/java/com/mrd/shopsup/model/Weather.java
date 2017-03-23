package com.mrd.shopsup.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mayurdube on 22/03/17.
 */

public class Weather implements Serializable {

    private String clouds;

    private String dt;

    private String humidity;

    private String pressure;

    private String speed;

    private String deg;

    private WeatherDescription[] weather;

    private Temp temp;

    public String getClouds ()
    {
        return clouds;
    }

    public void setClouds (String clouds)
    {
        this.clouds = clouds;
    }

    public String getDt ()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d");
        return sdf.format(new Date(Long.parseLong(dt)*1000));
    }

    public void setDt (String dt)
    {
        this.dt = dt;
    }

    public String getHumidity ()
    {
        return humidity;
    }

    public void setHumidity (String humidity)
    {
        this.humidity = humidity;
    }

    public String getPressure ()
    {
        return pressure;
    }

    public void setPressure (String pressure)
    {
        this.pressure = pressure;
    }

    public String getSpeed ()
    {
        return speed;
    }

    public void setSpeed (String speed)
    {
        this.speed = speed;
    }

    public String getDeg ()
    {
        return deg;
    }

    public void setDeg (String deg)
    {
        this.deg = deg;
    }

    public WeatherDescription[] getWeather ()
    {
        return weather;
    }

    public void setWeather (WeatherDescription[] weather)
    {
        this.weather = weather;
    }

    public Temp getTemp ()
    {
        return temp;
    }

    public void setTemp (Temp temp)
    {
        this.temp = temp;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [clouds = "+clouds+", dt = "+dt+", humidity = "+humidity+", pressure = "+pressure+", speed = "+speed+", deg = "+deg+", weather = "+weather+", temp = "+temp+"]";
    }
}
