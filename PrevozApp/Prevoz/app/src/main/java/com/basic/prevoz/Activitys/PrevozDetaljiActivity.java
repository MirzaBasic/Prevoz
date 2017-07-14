package com.basic.prevoz.Activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.prevoz.Firebase.NotificationFirebaseMessageService;
import com.basic.prevoz.Helper.DateConverter;
import com.basic.prevoz.Helper.GsonConverter;
import com.basic.prevoz.Helper.ImageConverter;
import com.basic.prevoz.Helper.MyRunnable;
import com.basic.prevoz.Models.GoogleMapsModels.Direction;
import com.basic.prevoz.Models.NotificationTextVM;
import com.basic.prevoz.Models.GoogleMapsModels.Route;
import com.basic.prevoz.Models.NotificationVM;
import com.basic.prevoz.Models.PrevozVM;
import com.basic.prevoz.Models.ZahtjevZaPrevozVM;
import com.basic.prevoz.R;
import com.basic.prevoz.Helper.DirectionConverter;
import com.basic.prevoz.Utils.GoogleMapsUtils;
import com.basic.prevoz.Utils.NetworkUtils;
import com.basic.prevoz.Utils.Sesija;
import com.facebook.internal.Utility;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import com.google.android.gms.maps.model.MarkerOptions;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class PrevozDetaljiActivity extends AppCompatActivity implements OnMapReadyCallback {
    private PrevozVM mPrevoz;
    private Direction mDirections;
    private static String USER_KEY="user_key";
    private static String PREVOZ_KEY="prevoz_key";
    private Toolbar mToolbar;
    final OnMapReadyCallback onMapReadyCallback =this;
    private  MapFragment mapFragment;

    public PrevozDetaljiActivity() {
        mapFragment = new MapFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_prevoz_detalji);




        mToolbar= (Toolbar) findViewById(R.id.toolbar_detalji);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left_thick);



        Bundle arg=getIntent().getExtras();
        if(arg!=null){
            if(arg.containsKey(PREVOZ_KEY)){

                mPrevoz= (PrevozVM) arg.getSerializable(PREVOZ_KEY);

                doSetDetaljiPrevoza();

            }



            mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

            doGetRoute(new MyRunnable<String>() {
                @Override
                public void run(String result) {
                    Type listType = new TypeToken<ArrayList<Direction>>() {
                    }.getType();

                    mDirections= GsonConverter.JsonToObject(result,Direction.class);
                    mapFragment.getMapAsync(onMapReadyCallback);


                }
            });


        }
    }

    private void doSetDetaljiPrevoza() {

        TextView mUsername= (TextView) findViewById(R.id.tv_username);

        TextView mEmail= (TextView) findViewById(R.id.tv_email);
        final  ImageView mUserImage= (ImageView) findViewById(R.id.image_user);
        TextView mStartGrad= (TextView) findViewById(R.id.tv_start_grad);
        TextView mStanice= (TextView) findViewById(R.id.tv_stanice);
        TextView mKrajGrad= (TextView) findViewById(R.id.tv_kraj_grad);
        TextView mOpis= (TextView) findViewById(R.id.tv_opis);
        TextView mDatumKretanja= (TextView) findViewById(R.id.tv_datum);
        TextView mVrijemeKretanaj= (TextView) findViewById(R.id.tv_vrijeme);
        String stanice="";

        final int PIXEL_SIZE=100;
        mUsername.setText(mPrevoz.Korisnik.ImePrezime);
        mUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOpenProfile();
            }
        });
        mUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOpenProfile();
            }
        });
        mEmail.setText(mPrevoz.Korisnik.Email);



        if(mPrevoz.Korisnik.photoUrl!=null){

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
                    if(result!=null) {
                        Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(result, PIXEL_SIZE);

                        mUserImage.setImageBitmap(circularBitmap);

                    }
                }
            }.execute(Uri.parse(mPrevoz.Korisnik.photoUrl).buildUpon().appendQueryParameter("sz", String.valueOf(PIXEL_SIZE)).build());
        }


        for (int i=0;i<mPrevoz.Stanice.size();i++){

            if(i==0){
                mStartGrad.setText(mPrevoz.Stanice.get(i).Grad);
            }
            else if(i==mPrevoz.Stanice.size()-1){

                mKrajGrad.setText(mPrevoz.Stanice.get(i).Grad);

            }
            else{
                if(i==1){
                    stanice=mPrevoz.Stanice.get(i).Grad;
                }
                else {
                    stanice += ", " + mPrevoz.Stanice.get(i).Grad;
                }
            }


        }
        mStanice.setText(stanice);



        mDatumKretanja.setText(DateConverter.to_dd_mm_yyyy(mPrevoz.DatumKretanja));
        mVrijemeKretanaj.setText(DateConverter.to_hh_mm(mPrevoz.DatumKretanja));
        mOpis.setText(mPrevoz.Opis);





        Button mButtonSendDriveRequest= (Button) findViewById(R.id.btn_posalji_zahtjev);
        mButtonSendDriveRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                doSendDriveRequest(new MyRunnable<Boolean>() {
                    @Override
                    public void run(Boolean success) {
                        if (success) {
                            Snackbar.make(v, getString(R.string.drive_request_send_successful), Snackbar.LENGTH_LONG)
                                    .show();


                        } else {
                            Snackbar.make(v, getString(R.string.drive_request_send_error), Snackbar.LENGTH_LONG)
                                    .show();

                        }
                    }
                });


            }
        });




    }

    private void doOpenProfile() {
        Intent intent=new Intent(PrevozDetaljiActivity.this,ProfilActivity.class);
        intent.putExtra(USER_KEY,mPrevoz.Korisnik.Id);
        startActivity(intent);
    }


    private void doSendDriveRequest(final MyRunnable<Boolean> onSuccess) {

       final ZahtjevZaPrevozVM zzp=new ZahtjevZaPrevozVM();
        zzp.Kolicina=1;
        zzp.KorisnikId=Sesija.GetSignInUser().Id;
        zzp.PrevozId=mPrevoz.Id;

        new AsyncTask<URL, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(URL... params) {
             Boolean success=true;
                try {
                    NetworkUtils.postResponseToHttpUrl(params[0],GsonConverter.ObjectToJson(zzp));
                } catch (IOException e) {
                    e.printStackTrace();
                    success=false;
                }
                return success;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
              onSuccess.run(success);
            }
        }.execute(NetworkUtils.buildSendDriveRequestURL());
    }


    private void doCallGoogleNavigation() {
        String GOOGLE_MAPS_BASIC_URL="http://maps.google.com/maps";
        String navigationString=GOOGLE_MAPS_BASIC_URL+"?saddr="+mPrevoz.Stanice.get(0).Grad;


        for (int i=1;i<mPrevoz.Stanice.size();i++) {
            if(i==1){
                navigationString=navigationString+"&daddr="+ mPrevoz.Stanice.get(i).Grad;
            }
            if(i>1){
                navigationString = navigationString +"+to:" + mPrevoz.Stanice.get(i).Grad;
            }
        }




        Intent navigationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(navigationString));
        if(navigationIntent.resolveActivity(getPackageManager())!=null)
        {
            startActivity(navigationIntent);
        }
        else{

            Toast.makeText(this,getString(R.string.message_install_google_maps),Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        int widthRouteLine=5;

        List<MarkerOptions> margers=new ArrayList<>();


        for (int i = 0; i < mDirections.getRouteList().size(); i++) {
            Route route = mDirections.getRouteList().get(i);

            for (int j = 0; j < route.getLegList().size(); j++) {
                ArrayList<LatLng> directionPositionList = route.getLegList().get(j).getDirectionPoint();


                //Markeri za gradove
                MarkerOptions markerStart=new  MarkerOptions()
                        .position(route.getLegList().get(j).getStartLocation().getCoordination());

                MarkerOptions markerEnd=new  MarkerOptions()
                        .position(route.getLegList().get(j).getEndLocation().getCoordination());

                //Dodaje markere u listu da bi izracunao centar za pozicju kamere
                margers.add(markerStart);
                margers.add(markerEnd);

                //Dodaje markere na mapu
                googleMap.addMarker(markerStart);
                googleMap.addMarker(markerEnd);

                //Označavanje rute na mapi
                googleMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, widthRouteLine, Color.RED));



            }


        }

        if(margers.size()!=0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (MarkerOptions marker : margers) {
                builder.include(marker.getPosition());
            }
            LatLngBounds bounds = builder.build();

            int displayWidth = getResources().getDisplayMetrics().widthPixels;
            int padding = (int) (displayWidth * 0.15);

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            googleMap.animateCamera(cu);

        }
    }

    public void doGetRoute(final MyRunnable runnable){

        new AsyncTask<URL, Void, String>() {
            @Override
            protected String doInBackground(URL... params) {
                String result = "";
                try {
                    result = NetworkUtils.getResponseFromHttpUrl(params[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                    result = null;
                }
                return result;

            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result != null) {


                    runnable.run(result);

                }
            }


        }.execute(GoogleMapsUtils.buildDirectionURL(mPrevoz.Stanice));
    }




}

