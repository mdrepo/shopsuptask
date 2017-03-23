package com.mrd.shopsup.ui;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.mrd.shopsup.R;
import com.mrd.shopsup.controllers.WeatherController;
import com.mrd.shopsup.model.Weather;
import com.mrd.shopsup.utils.Utils;

import java.util.ArrayList;

import static com.mrd.shopsup.controllers.WeatherController.GET_WEATHER;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener,
        ViewPager.OnPageChangeListener, Handler.Callback, FragmentCallback {

    MenuItem prevMenuItem;
    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    ProgressDialog mProgressDialog;
    View root;

    Handler mHandler;
    WeatherController controller;

    // City fragments
    WeatherFragment fragDelhi;
    WeatherFragment fragBangalore;
    WeatherFragment fragChennai;

    ArrayList<Weather> weatherBa = new ArrayList<>();
    ArrayList<Weather> weatherChennai = new ArrayList<>();
    ArrayList<Weather> weatherDelhi = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        mHandler = new Handler(this);
        controller = new WeatherController(getApplicationContext());
    }


    private void initUI() {
        root = findViewById(R.id.activity_main);
        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Fetching data");
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bengaluru:
                /*if (!fetchingBangaloreWeather && fragBangalore.getWeatherList() == null) {
                    fetchWeather(Utils.WEATHERMAP_BANGLORE_ID);
                    fetchingBangaloreWeather = true;
                }*/
                viewPager.setCurrentItem(0);
                break;
            case R.id.action_delhi:
//                if (!fetchingDelhiWeather && fragDelhi.getWeatherList() == null) {
//                    fetchWeather(Utils.WEATHERMAP_DELHI_ID);
//                    fetchingDelhiWeather = true;
//                }
                viewPager.setCurrentItem(1);
                break;
            case R.id.action_chennai:
//                if (!fetchingChennaiWeather && fragChennai.getWeatherList() == null) {
//                    fetchWeather(Utils.WEATHERMAP_CHENNAI_ID);
//                    fetchingChennaiWeather = true;
//                }
                viewPager.setCurrentItem(2);
                break;

        }
        return true;
    }

    private void fetchWeather(int city) {
        controller.handleMessage(GET_WEATHER, city);
        if(mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragChennai = WeatherFragment.newInstance(weatherChennai,Utils.WEATHERMAP_CHENNAI_ID);
        fragDelhi = WeatherFragment.newInstance(weatherDelhi,Utils.WEATHERMAP_DELHI_ID);
        fragBangalore = WeatherFragment.newInstance(weatherBa,Utils.WEATHERMAP_BANGLORE_ID);
        adapter.addFragment(fragBangalore);
        adapter.addFragment(fragDelhi);
        adapter.addFragment(fragChennai);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (prevMenuItem != null) {
            prevMenuItem.setChecked(false);
        } else {
            bottomNavigationView.getMenu().getItem(0).setChecked(false);
        }
        bottomNavigationView.getMenu().getItem(position).setChecked(true);
        prevMenuItem = bottomNavigationView.getMenu().getItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean handleMessage(final Message message) {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        switch (message.what) {
            case GET_WEATHER:
                if (message.arg1 == Utils.WEATHERMAP_BANGLORE_ID) {
                    fragBangalore.setWeatherList((ArrayList<Weather>) message.obj);
                } else if (message.arg1 == Utils.WEATHERMAP_CHENNAI_ID) {
                    fragChennai.setWeatherList((ArrayList<Weather>) message.obj);
                } else if (message.arg1 == Utils.WEATHERMAP_DELHI_ID) {
                    fragDelhi.setWeatherList((ArrayList<Weather>) message.obj);
                }
                break;
            case WeatherController.MESSAGE_NO_DATA:
            case WeatherController.MESSAGE_NO_NETWORK:
                switch (message.arg1) {
                    case Utils.WEATHERMAP_BANGLORE_ID:
                        fragBangalore.setError((String)message.obj);
                        break;
                    case Utils.WEATHERMAP_CHENNAI_ID:
                        fragChennai.setError((String)message.obj);
                        break;
                    case Utils.WEATHERMAP_DELHI_ID:
                        fragDelhi.setError((String)message.obj);
                        break;
                }
                break;
        }

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        controller.addOutboxHandler(mHandler);
    }

    @Override
    protected void onPause() {
        super.onPause();
        controller.removeOutboxHandler(mHandler);
    }

    @Override
    public void doAction(int action, Object paramter) {
        switch(action) {
            case GET_WEATHER:
                fetchWeather((int) paramter);
                break;
        }

    }
}
