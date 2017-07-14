package com.basic.prevoz.Helper;


import android.content.Context;
import android.content.Intent;

import com.basic.prevoz.LoginActivity;
import com.basic.prevoz.NavigationDrawerActivity;
import com.basic.prevoz.Utils.Sesija;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;

/**
 * Created by Developer on 31.05.2017..
 */

public class GoogleApi {



    public static GoogleApiClient createClient(Context context){

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail().requestProfile()
            .build();
   GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(context)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso).addApi(Plus.API)
            .build();
    mGoogleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);

return mGoogleApiClient;


    }





}
