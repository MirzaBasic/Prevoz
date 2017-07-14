package com.basic.prevoz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.basic.prevoz.Activitys.MessageActivity;
import com.basic.prevoz.Activitys.PrevozDetaljiActivity;
import com.basic.prevoz.Activitys.ProfilActivity;
import com.basic.prevoz.Fragments.PrevoziViewPagerFragment;
import com.basic.prevoz.Activitys.PrijateljiActivity;
import com.basic.prevoz.Helper.GoogleApi;
import com.basic.prevoz.Helper.ImageConverter;
import com.basic.prevoz.Models.KorisniciVM;
import com.basic.prevoz.Models.PrijateljiAdapter;
import com.basic.prevoz.Utils.NetworkUtils;
import com.basic.prevoz.Utils.Sesija;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.net.URL;

public class NavigationDrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static int PIXEL_SIZE = 200;
    private static String PREVOZI_TOPIC = "prevozi";

    private DrawerLayout mDrawer;
    private View mHeaderNav;
    private ImageView userImage;
    private GoogleApiClient mGoogleApiClient;
    private static String USER_KEY = "user_key";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);


        mGoogleApiClient = GoogleApi.createClient(this);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        PrevoziViewPagerFragment fragment = new PrevoziViewPagerFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_layout, fragment).commit();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mHeaderNav = navigationView.getHeaderView(0);

        userImage = (ImageView) mHeaderNav.findViewById(R.id.image_user);
        TextView username = (TextView) mHeaderNav.findViewById(R.id.tv_username);
        TextView email = (TextView) mHeaderNav.findViewById(R.id.tv_email);

        KorisniciVM korisnik = Sesija.GetSignInUser();


        username.setText(korisnik.ImePrezime);
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOpenProfile();
            }
        });
        email.setText(korisnik.Email);

        if (korisnik.photoUrl != null) {
            doSetUserImage(korisnik.photoUrl);
            userImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doOpenProfile();
                }
            });
        }

        if (korisnik.coverPhotoUrl != null) {
            doSetUserCoverImage(korisnik.coverPhotoUrl);
        }



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
            finish();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_prevozi) {
            PrevoziViewPagerFragment fragment = new PrevoziViewPagerFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_layout, fragment).commit();

            getSupportActionBar().setTitle("TNP");

        }
        if(id==R.id.nav_messanger){


        Intent intent=new Intent(NavigationDrawerActivity.this,MessageActivity.class);
            startActivity(intent);


        }

        if(id==R.id.nav_prijatelji){
            Intent intent=new Intent(NavigationDrawerActivity.this,PrijateljiActivity.class);
            startActivity(intent);






        }
        if (id == R.id.nav_logout) {
            doLogout();
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void OpenDrawer() {

        mDrawer.openDrawer(GravityCompat.START);
    }

    public void doSetUserImage(String url) {

        new AsyncTask<Uri, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Uri... params) {
                try {
                    return NetworkUtils.getBitmapImageFromUri(params[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                if (result != null) {
                    Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(result, PIXEL_SIZE);

                    userImage.setImageBitmap(circularBitmap);
                }

            }
        }.execute(Uri.parse(url).buildUpon().appendQueryParameter("sz", String.valueOf(PIXEL_SIZE)).build());


    }

    public void doSetUserCoverImage(String url) {

        new AsyncTask<Uri, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Uri... params) {
                try {
                    return NetworkUtils.getBitmapImageFromUri(params[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap result) {
                super.onPostExecute(result);
                if (result != null) {
                    Drawable drawableCoverImage = new BitmapDrawable(result);

                    mHeaderNav.setBackground(drawableCoverImage);


                }

            }
        }.execute(Uri.parse(url).buildUpon().build());


    }

    private void doLogout() {

        FirebaseMessaging.getInstance().unsubscribeFromTopic(PREVOZI_TOPIC);
        doDeleteFirebaseToken();


        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {


                    }
                });

        LoginManager.getInstance().logOut();


        Sesija.SaveSignInUser(null);
        Intent intent = new Intent(NavigationDrawerActivity.this, LoginActivity.class);
        startActivity(intent);

        finish();


    }

    private void doDeleteFirebaseToken() {





        new AsyncTask<URL, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(URL... params) {
                Boolean success = true;
                try {
                    NetworkUtils.postResponseToHttpUrl(params[0], "");
                } catch (IOException e) {
                    success = false;
                    e.printStackTrace();
                }
                return success;
            }


        @Override
        protected void onPostExecute (Boolean aBoolean){
            super.onPostExecute(aBoolean);


        }
    }.execute(NetworkUtils.buildDeleteFirebaseTokenURL(Sesija.GetSignInUser().FirebaseTokenId));
}

    @Override
    protected void onRestart() {
        super.onRestart();
        mGoogleApiClient.connect(GoogleApiClient.SIGN_IN_MODE_OPTIONAL);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mGoogleApiClient.isConnected()){
       mGoogleApiClient.disconnect();
    }
    }
    private void doOpenProfile() {
        Intent intent=new Intent(NavigationDrawerActivity.this,ProfilActivity.class);
        intent.putExtra(USER_KEY,Sesija.GetSignInUser().Id);
        startActivity(intent);
    }


}
