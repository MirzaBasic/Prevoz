package com.basic.prevoz.Activitys.DodajNoviPrevoz;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.basic.prevoz.Helper.ImageConverter;
import com.basic.prevoz.Helper.MyAnimations;
import com.basic.prevoz.Models.Global;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.basic.prevoz.Helper.GsonConverter;
import com.basic.prevoz.Helper.StaniceListIconLetter;
import com.basic.prevoz.Models.PrevozVM;
import com.basic.prevoz.Models.StanicaVM;
import com.basic.prevoz.R;
import com.basic.prevoz.Utils.NetworkUtils;
import com.basic.prevoz.Utils.Sesija;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NoviPrevozStaniceActivity extends AppCompatActivity {

    private ListView mListViewStanice;
    private BaseAdapter mAdapterStanice;
    private int MAX_STANICA=10;
    private List<StanicaVM> mStaniceList;
    private static int STANICA_ITEM_ID;
    private static int PLACE_PICKER_REQUEST = 99;
    private FloatingActionButton mButtonNext;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novi_prevoz_stanice);
        mToolbar= (Toolbar) findViewById(R.id.toolbar_stanice);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mButtonNext= (FloatingActionButton) findViewById(R.id.button_next);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doButtonNextClick();
            }
        });
        mStaniceList=new ArrayList<>();
         if(Global.noviPrevoz.Stanice!=null){
         mStaniceList.addAll(Global.noviPrevoz.Stanice);

        }
        else {
             mStaniceList.add(new StanicaVM());
             mStaniceList.add(new StanicaVM());
         }

        mListViewStanice= (ListView) findViewById(R.id.listview_stanice);

        doSetStaniceListAdapter();

        mListViewStanice.setAdapter(mAdapterStanice);






        doValidateForm();

    }



    private void doButtonNextClick() {


        Intent intent=new Intent(NoviPrevozStaniceActivity.this,NoviPrevozVrijemeActivity.class);

        startActivity(intent);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
      if(mStaniceList.size()>=MAX_STANICA){

        menu.findItem(R.id.action_dodaj_stanicu).setEnabled(false).getIcon().setAlpha(100);

      }
      else {
          menu.findItem(R.id.action_dodaj_stanicu).setEnabled(true).getIcon().setAlpha(255);
      }
      return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stanice,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id==R.id.action_dodaj_stanicu){


            mStaniceList.add(new StanicaVM());

            mAdapterStanice.notifyDataSetChanged();
            doValidateForm();
            invalidateOptionsMenu();




        }
        return super.onOptionsItemSelected(item);
    }

    private void doSetStaniceListAdapter() {
        mAdapterStanice=new BaseAdapter() {
            @Override
            public int getCount() {
                return mStaniceList.size();
            }

            @Override
            public Object getItem(int position) {
                return mStaniceList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(final int position, View view, ViewGroup parent) {

               mStaniceList.get(position).RednaOdznaka=position+1;
                if(view==null){
                    LayoutInflater inflater= (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view=inflater.inflate(R.layout.stanice_list_item,parent,false);

                }
                TextView mTextViewStaniceIcon= (TextView) view.findViewById(R.id.textview_stanice_icon);
                mTextViewStaniceIcon.setText(StaniceListIconLetter.getLetter(position));
                Button mButtonOdaberiStanice= (Button) view.findViewById(R.id.button_stanica);
                mButtonOdaberiStanice.setText(mStaniceList.get(position).Grad);

                mButtonOdaberiStanice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        STANICA_ITEM_ID=position;
                     doCallPlacePicker();
                    }
                });


                ImageButton mButtonDeleteStanicu= (ImageButton) view.findViewById(R.id.button_delete_stanicu);

                mButtonDeleteStanicu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mStaniceList.size()>2){
                        mStaniceList.remove(position);
                        mListViewStanice.setAdapter(mAdapterStanice);
                            doValidateForm();
                        }
                    }
                });

                return view;
            }
        };

    }
    private void doCallPlacePicker() {


        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(this);

            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                    mStaniceList.get(STANICA_ITEM_ID).Grad=place.getName().toString();
                mStaniceList.get(STANICA_ITEM_ID).Lat=place.getLatLng().latitude;
                mStaniceList.get(STANICA_ITEM_ID).Lng=place.getLatLng().longitude;
                mListViewStanice.setAdapter(mAdapterStanice);
                Global.noviPrevoz.Stanice=mStaniceList;
                doValidateForm();
                }


            }
        }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void doValidateForm(){
        invalidateOptionsMenu();
        boolean valid=true;

        for (StanicaVM stanica:mStaniceList) {
            if(stanica.Grad==null){
                valid=false;


            }
        }
        if(valid){
            MyAnimations.showFloatingButton(mButtonNext);
        }
        else{

            MyAnimations.hideFloatingButton(mButtonNext);
        }
    }
}
