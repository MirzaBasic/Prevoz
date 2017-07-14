package com.basic.prevoz.Helper;


import android.app.Application;
import android.content.Context;

/**
 * Created by Developer on 23.05.2017..
 */

public class MyApp extends Application
{

    public static Context getContext()
    {
        return context;
    }

    private static Context context;

    @Override
    public void onCreate()
    {
        super.onCreate();

        context = getApplicationContext();
        //custom code
    }

}
