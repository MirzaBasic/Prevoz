package com.basic.prevoz.Activitys;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.basic.prevoz.Helper.GsonConverter;
import com.basic.prevoz.Models.MessageAdapter;
import com.basic.prevoz.Models.PorukeVM;
import com.basic.prevoz.R;
import com.basic.prevoz.Utils.NetworkUtils;
import com.basic.prevoz.Utils.Sesija;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,MessageAdapter.MessageAdapterOnClickHandler {

   private MessageAdapter mAdapter;
    private static int LOADER_ID=79;
    private static final String SEARCH_QUERY_RESULT_KEY = "search_url_result";
    private RecyclerView mRecyclerView;
    private TextView mSearchNotFound;
    private TextView mErrorMessage;
    private ProgressBar mProggresBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view_messages);
        mErrorMessage = (TextView) findViewById(R.id.textviev_error_message);
        mProggresBar = (ProgressBar) findViewById(R.id.proggres_bar);
        mSearchNotFound= (TextView) findViewById(R.id.textviev_search_not_found_message);

        int recyclerViewOrientation = LinearLayoutManager.VERTICAL;
        boolean shouldReverseLayout = false;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, recyclerViewOrientation, shouldReverseLayout);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter=new MessageAdapter(this);

        if(savedInstanceState!=null){


            List<PorukeVM> messages =(List<PorukeVM>)savedInstanceState.getSerializable(SEARCH_QUERY_RESULT_KEY);
            if(messages!=null){

                mAdapter.setMessagesData(messages);
                mRecyclerView.setAdapter(mAdapter);
            }
        }


        LoaderManager loaderManager=getSupportLoaderManager();
        loaderManager.initLoader(LOADER_ID,null,this);


    }
    public void doShowErrorMessage() {
        mSearchNotFound.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }
    public void doShowSearchNotFound() {

        mSearchNotFound.setVisibility(View.VISIBLE);
    }
    public void doShowData() {
        mSearchNotFound.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
    }

    @Override
    public void OnClick(PorukeVM message) {




    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
     return new AsyncTaskLoader<String>(this) {
         String mMessages;

         @Override
         protected void onStartLoading() {
             super.onStartLoading();

             if (mMessages != null) {
                 deliverResult(mMessages);
             } else {
                 mProggresBar.setVisibility(View.VISIBLE);
                 forceLoad();
             }
         }

         @Override
         public String loadInBackground() {
             String jsonResult = null;
             try {
                 jsonResult = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildSearchUserMessagesURL(Sesija.GetSignInUser().Id, ""));
             } catch (IOException e) {
                 e.printStackTrace();
             }
             return jsonResult;
         }

         @Override
         public void deliverResult(String data) {
             mMessages = data;
             super.deliverResult(data);
         }
     };

    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
mProggresBar.setVisibility(View.INVISIBLE);




        if(data!=null){
            List<PorukeVM> mMessages=new ArrayList<>();
            Type listType=new TypeToken<ArrayList<PorukeVM>>(){}.getType();
            mMessages= GsonConverter.JsonToListArray(data,listType);
            mAdapter.setMessagesData(mMessages);
            mRecyclerView.setAdapter(mAdapter);
            if(mMessages.size()==0){

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

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }
}
