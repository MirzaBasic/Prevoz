package com.basic.prevoz.Activitys;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.basic.prevoz.Helper.EndlessRecyclerViewScrollListener;
import com.basic.prevoz.Helper.GsonConverter;
import com.basic.prevoz.Helper.MyRunnable;
import com.basic.prevoz.Models.ChatAdapter;
import com.basic.prevoz.Models.KorisniciVM;
import com.basic.prevoz.Models.PorukeVM;

import com.basic.prevoz.Models.PrevozVM;
import com.basic.prevoz.R;
import com.basic.prevoz.Services.ChatMessageService;
import com.basic.prevoz.Utils.NetworkUtils;
import com.basic.prevoz.Utils.Sesija;
import com.dgreenhalgh.android.simpleitemdecoration.linear.EndOffsetItemDecoration;
import com.dgreenhalgh.android.simpleitemdecoration.linear.StartOffsetItemDecoration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.basic.prevoz.R.string.korisnik;

public class ChatActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, ChatAdapter.ChatAdapterOnClickHandler{
    private EditText mChatMessage;
    private static int LOADER_ID = 90;
    private RecyclerView mRecyclerView;
    private KorisniciVM mKorisnik;
    private ChatAdapter mAdapter;
    private EndlessRecyclerViewScrollListener scrollListener;
    private Toolbar mToolbar;
    private static String USER_KEY="user_key";
    private int mPage;
    private  ChatMessageService mChatMessageService;
   private int korisnikId;





    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChatMessageService.Stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mToolbar= (Toolbar) findViewById(R.id.toolbar_chat);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mChatMessageService=new ChatMessageService();

        mPage=0;
        mAdapter = new ChatAdapter(this);
        mRecyclerView= (RecyclerView) findViewById(R.id.recycle_view_chat);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new StartOffsetItemDecoration(30));
        mRecyclerView.addItemDecoration(new EndOffsetItemDecoration(30));
        int recyclerViewOrientation = LinearLayoutManager.VERTICAL;

        boolean shouldReverseLayout = true;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, recyclerViewOrientation, shouldReverseLayout);
        mRecyclerView.setLayoutManager(layoutManager);


        if (getIntent().hasExtra(USER_KEY)) {
          korisnikId = getIntent().getIntExtra(USER_KEY, 0);

            mChatMessageService.Start(korisnikId, new MyRunnable<String>() {
                @Override
                public void run(String data) {
                    NewMessage(data);
                }
            });
            doGetMessages();
            doGetUserData(korisnikId, new MyRunnable<KorisniciVM>() {
                @Override
                public void run(KorisniciVM korisnici) {
                    mKorisnik=korisnici;
                    getSupportActionBar().setTitle(mKorisnik.ImePrezime);

                }
            });

            mChatMessage = (EditText) findViewById(R.id.et_message);
            ImageView mSendMessage = (ImageView) findViewById(R.id.button_send_chat_message);
            mSendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mChatMessage.getText().toString().equals("")) {
                        doSendMessage();

                    }
                }
            });



            scrollListener=new EndlessRecyclerViewScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                    mPage=page;
                    doGetMessages();

                }
            };
            mRecyclerView.addOnScrollListener(scrollListener);
        }
    }

    private void doGetUserData(int korisnikId, final MyRunnable<KorisniciVM> onSuccess) {


        new AsyncTask<URL, Void, String>() {
            @Override
            protected String doInBackground(URL... params) {
                String result = "";
                try {
                    result = NetworkUtils.getResponseFromHttpUrl(params[0]);
                } catch (IOException e) {
                    result = null;
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result != null && !result.equals("")) {
                    mKorisnik = GsonConverter.JsonToObject(result, KorisniciVM.class);
                    onSuccess.run(mKorisnik);
                }
            }
        }.execute(NetworkUtils.buildGetDetaljiKorisnikaURL(korisnikId));

    }

    private void doGetMessages() {
        if (getSupportLoaderManager().getLoader(LOADER_ID) == null) {
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);

        }

    }


    private void doSendMessage() {

        final PorukeVM message=new PorukeVM();
        message.Text=mChatMessage.getText().toString();

        if(korisnikId==Sesija.GetSignInUser().Id){
            message.Status=1;
        }
        else {
            message.Status = 0;
        }
        message.KorisnikPoslaoId= Sesija.GetSignInUser().Id;
        message.KorisnikPrimioId=mKorisnik.Id;
        message.DatumKreiranja=new Date();

        mChatMessage.setText("");


        new AsyncTask<URL, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(URL... params) {
                Boolean success=true;
                try {
                    NetworkUtils.postResponseToHttpUrl(params[0], GsonConverter.ObjectToJson(message));
                } catch (IOException e) {
                    e.printStackTrace();
                    success=false;

                }
                return success;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);
                if(success){
                    message.Status=ChatAdapter.SENT;
                    mAdapter.addLastPoruka(message);
                    mRecyclerView.scrollToPosition(0);

                }
                else{
                    message.Status=ChatAdapter.NOT_SENT;
                    mAdapter.addLastPoruka(message);
                    mRecyclerView.scrollToPosition(0);

                }
            }
        }.execute(NetworkUtils.buildSendMessageURL());


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


                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {
                String jsonResult = "";

                try {
                    jsonResult=  NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildGetConversation(Sesija.GetSignInUser().Id,korisnikId, mPage));
                } catch (IOException e) {
                    jsonResult = null;
                    e.printStackTrace();
                }
                return jsonResult;
            }

            @Override
            public void deliverResult(String data) {
                super.deliverResult(data);
                mMessages = data;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if(data!=null) {

            List<PorukeVM> mPoruke = new ArrayList<>();
            Type listType = new TypeToken<ArrayList<PorukeVM>>(){
            }.getType();
            mPoruke = GsonConverter.JsonToListArray(data, listType);
            if (mPage == 0) {
                mAdapter.setPorukeData(mPoruke);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mAdapter.addPorukeDataa(mPoruke);
            }

            if (mPoruke.size() == 0) {
                doShowSearchNotFound();
            } else {
                doShowData();
            }


        } else {

            doShowErrorMessage();
        }
    }

    private void doShowErrorMessage() {

    }
    private void doShowErrorMessageNotSent() {

    }

    private void doShowrMessageSuccessfulySent() {
    }

    private void doShowData() {
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
    private void doShowSearchNotFound() {
    }

    @Override
    public void OnClick(PorukeVM poruka) {

    }


    public void NewMessage(String data){
        Type listType = new TypeToken<ArrayList<PorukeVM>>(){
        }.getType();
        List<PorukeVM> poruke= GsonConverter.JsonToListArray(data,listType);
        if(poruke.size()!=0){
            mAdapter.addNewPorukeDataa(poruke);
            mRecyclerView.scrollToPosition(0);
        }



    }



}

