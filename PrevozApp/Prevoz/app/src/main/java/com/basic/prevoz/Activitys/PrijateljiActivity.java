package com.basic.prevoz.Activitys;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.basic.prevoz.Helper.GsonConverter;
import com.basic.prevoz.Helper.MyApp;
import com.basic.prevoz.Models.KorisniciVM;
import com.basic.prevoz.Models.PrijateljiAdapter;
import com.basic.prevoz.Models.PrijateljiVM;
import com.basic.prevoz.R;
import com.basic.prevoz.Utils.NetworkUtils;
import com.basic.prevoz.Utils.Sesija;
import com.google.gson.reflect.TypeToken;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;



/**
 * Created by Developer on 28.06.2017..
 */

public class PrijateljiActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,PrijateljiAdapter.PrijateljiAdapterOnClickHandler {

    private PrijateljiAdapter mAdapter;
    private static int LOADER_ID=80;
    private static final String SEARCH_QUERY_RESULT_KEY = "search_url_result";
    private RecyclerView mRecyclerView;
    private TextView mSearchNotFound;
    private TextView mErrorMessage;
    private String mSearchQuery;
    private ProgressBar mProggresBar;
    private MaterialSearchView mSearchView;

    private static String USER_KEY="user_key";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prijatelji);

        Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar_prijatelji);
      setSupportActionBar(toolbar);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mSearchView= (MaterialSearchView) findViewById(R.id.search_view);
        mSearchView.setVoiceSearch(true);
        mSearchQuery="";



        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view_messages);
        mErrorMessage = (TextView) findViewById(R.id.textviev_error_message);
        mProggresBar = (ProgressBar) findViewById(R.id.proggres_bar);
        mSearchNotFound= (TextView) findViewById(R.id.textviev_search_not_found_message);

        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchQuery=query;
                doSearchData();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mSearchQuery=newText;
                doSearchData();
                return true;
            }
        });
        mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        int recyclerViewOrientation = LinearLayoutManager.VERTICAL;
        boolean shouldReverseLayout = false;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, recyclerViewOrientation, shouldReverseLayout);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter=new PrijateljiAdapter(this);
        if(savedInstanceState!=null){


            List<KorisniciVM> prijatelji =(List<KorisniciVM>)savedInstanceState.getSerializable(SEARCH_QUERY_RESULT_KEY);
            if(prijatelji!=null){

                mAdapter.setPrijateljiData(prijatelji);
                mRecyclerView.setAdapter(mAdapter);
            }
        }


        LoaderManager loaderManager=getSupportLoaderManager();
        loaderManager.initLoader(LOADER_ID,null,this);

    }



    public void doSearchData(){
        if (getSupportLoaderManager().getLoader(LOADER_ID) == null) {
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.prijatelji, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);

        return true;
    }



    @Override
    public void OnClick(KorisniciVM prijatelj) {

        Intent intent =new Intent(MyApp.getContext(),ProfilActivity.class);
        intent.putExtra(USER_KEY,prijatelj.Id);
        startActivity(intent);
    }



    public void doShowErrorMessage() {

        mRecyclerView.setVisibility(View.INVISIBLE);
        Snackbar.make(findViewById(R.id.content), getString(R.string.error_message), Snackbar.LENGTH_INDEFINITE).
                setAction(getString(R.string.refresh), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        doSearchData();
                    }
                }).setActionTextColor(Color.RED)
                .show();

    }
    public void doShowSearchNotFound() {
        Snackbar.make(findViewById(R.id.content), getString(R.string.message_friend_search_not_found), Snackbar.LENGTH_SHORT).
                show();

    }
    public void doShowData() {

        mRecyclerView.setVisibility(View.VISIBLE);

    }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {

        return new AsyncTaskLoader<String>(this) {
            String mPrijatelji;
            @Override
            protected void onStartLoading() {

                super.onStartLoading();
                if(mPrijatelji!=null){

                    deliverResult(mPrijatelji);
                }
                else{
                mProggresBar.setVisibility(View.VISIBLE);
                forceLoad();
                }
            }

            @Override
            public String loadInBackground() {
                String jsonResult = "";
                try {
                    jsonResult = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildSearchUserFriendsURL(Sesija.GetSignInUser().Id, mSearchQuery,1));
                } catch (IOException e) {
                    jsonResult=null;
                    e.printStackTrace();
                }
                return jsonResult;
            }

            @Override
            public void deliverResult(String data) {
                super.deliverResult(data);
                mPrijatelji=data;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
     mProggresBar.setVisibility(View.INVISIBLE);

        if(data!=null){
            List<KorisniciVM> mPrijatelji=new ArrayList<>();
            Type listType=new TypeToken<ArrayList<KorisniciVM>>(){}.getType();
            mPrijatelji= GsonConverter.JsonToListArray(data,listType);
            mAdapter.setPrijateljiData(mPrijatelji);
            mRecyclerView.setAdapter(mAdapter);
            if(mPrijatelji.size()==0){

                doShowSearchNotFound();
            }
            else{
                doShowData();
            }
        }
        else{

            doShowErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
