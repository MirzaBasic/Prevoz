package com.basic.prevoz.Utils;

import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Developer on 19.06.2017..
 */

public class FacebookApiUtils {
    private static final String TAG = FacebookApiUtils.class.getSimpleName();
    public static String BASIC_GRAPH_URL = "https://graph.facebook.com/";


    private static String COVER="cover";
    private static String FIELDS="fields";
    private static String ACCESS_TOKEN="access_token";
    public static URL buildGraphFacebookCoverURL(String userId, String accessToken) {
        Uri uri = Uri.parse(BASIC_GRAPH_URL+userId).buildUpon().


                appendQueryParameter(FIELDS, COVER).
                appendQueryParameter(ACCESS_TOKEN,accessToken).
                build();


        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built url" + url.toString());
        return url;


    }

}
