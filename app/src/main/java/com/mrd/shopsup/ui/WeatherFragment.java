package com.mrd.shopsup.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mrd.shopsup.R;
import com.mrd.shopsup.controllers.WeatherController;
import com.mrd.shopsup.model.Weather;
import com.mrd.shopsup.utils.Utils;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class WeatherFragment extends Fragment {

    private static final String ARG_WEATHER_LIST = "weather-list";
    private static final String ARG_CITY_ID = "city-id";
    ArrayList<Weather> mWeatherList = null;
    WeatherListAdapter mAdapter;
    Context mContext;
    private FragmentCallback mFragmentCallback;
    RecyclerView recyclerView;
    private int mCityId;
    View vwError;
    TextView txtError;
    Button btnError;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WeatherFragment() {
    }

    @SuppressWarnings("unused")
    public static WeatherFragment newInstance(ArrayList<Weather> weatherList, int cityId) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_WEATHER_LIST, weatherList);
        args.putInt(ARG_CITY_ID, cityId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mWeatherList = (ArrayList<Weather>) getArguments().getSerializable(ARG_WEATHER_LIST);
            mCityId = getArguments().getInt(ARG_CITY_ID);
        }

        if (mWeatherList != null) {
            mFragmentCallback.doAction(WeatherController.GET_WEATHER, mCityId);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_list, container, false);
        mContext = view.getContext();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        vwError = view.findViewById(R.id.vw_error);
        txtError = (TextView) view.findViewById(R.id.txt_error);
        btnError = (Button) view.findViewById(R.id.btnretry);
        if(mWeatherList != null && mWeatherList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            vwError.setVisibility(View.GONE);
            mAdapter = new WeatherListAdapter(mWeatherList);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        }

    }

    public void setError(String error) {
        recyclerView.setVisibility(View.GONE);
        vwError.setVisibility(View.VISIBLE);
        txtError.setText(error);
        btnError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFragmentCallback.doAction(WeatherController.GET_WEATHER, mCityId);
            }
        });
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragmentCallback = (FragmentCallback) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setWeatherList(ArrayList<Weather> obj) {
        mWeatherList = obj;
        if (recyclerView != null && mWeatherList != null && mWeatherList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            vwError.setVisibility(View.GONE);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            mAdapter = new WeatherListAdapter(mWeatherList);
            recyclerView.setAdapter(mAdapter);
        }
    }

    public ArrayList<Weather> getWeatherList() {
        return mWeatherList;
    }
}
