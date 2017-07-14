package com.basic.prevoz.Utils;

import android.net.Uri;
import android.util.Log;

import com.basic.prevoz.Models.StanicaVM;
import com.basic.prevoz.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by Developer on 15.06.2017..
 */

public class GoogleMapsUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    public static String BASIC_DIRECTION_URL="https://maps.googleapis.com/maps/api/directions/json";
public static String ORIGIN="origin";
    public static String DESTINATION="destination";
    public static String WAYPOINTS="waypoints";
    public static String API_KEY="AIzaSyD0mwUDfA8zY3X5E_4BA6nE7wq6ajo8xzY";
public static String KEY="key";

public static URL buildDirectionURL(List<StanicaVM> stanice){


          String waypoints="";
          String destination="";
          String origin="";
            for(int i=0;i<stanice.size();i++){

              if(i==0){
                 origin=stanice.get(i).Lat+","+stanice.get(i).Lng;
              }
              else if(i==stanice.size()-1){
                  destination=stanice.get(i).Lat+","+stanice.get(i).Lng;
              }
              else{

                  if (waypoints.equals("")) {

                      waypoints=stanice.get(i).Lat+","+stanice.get(i).Lng;
                  }
                  else{

                      waypoints+="|"+stanice.get(i).Lat+","+stanice.get(i).Lng;
                  }
                  }
              }

    Uri uri = Uri.parse(BASIC_DIRECTION_URL).buildUpon().

            appendQueryParameter(ORIGIN,origin).
            appendQueryParameter(DESTINATION,destination ).
            appendQueryParameter(WAYPOINTS,waypoints).
            appendQueryParameter(KEY, API_KEY).

            build();



           uri.buildUpon().build();


    URL url = null;
    try {
        url = new URL(uri.toString());
    } catch (MalformedURLException e) {
        e.printStackTrace();
    }
    Log.v(TAG, "Built url" + url.toString());
    return url;



}}