package com.basic.prevoz;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.basic.prevoz.Utils.Sesija;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends Activity{
    protected static  String NOVI_PREVOZI="prevozi";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        if(Sesija.GetSignInUser()!=null) {
            Intent intent = new Intent(MainActivity.this, NavigationDrawerActivity.class);
            startActivity(intent);


        }
else{
           Intent intent=new Intent(MainActivity.this ,LoginActivity.class);

           startActivity(intent);

}

        finish();
    }


}


