package com.basic.prevoz.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.basic.prevoz.Activitys.PrevozDetaljiActivity;
import com.basic.prevoz.Helper.EndlessRecyclerViewScrollListener;
import com.basic.prevoz.Helper.GsonConverter;
import com.basic.prevoz.Helper.MyApp;
import com.basic.prevoz.Helper.MyRunnablePar;
import com.basic.prevoz.Models.PrevozAdapter;
import com.basic.prevoz.Models.PrevozVM;
import com.basic.prevoz.Utils.NetworkUtils;
import com.dgreenhalgh.android.simpleitemdecoration.linear.EndOffsetItemDecoration;
import com.dgreenhalgh.android.simpleitemdecoration.linear.StartOffsetItemDecoration;
import com.google.gson.reflect.TypeToken;
import com.basic.prevoz.R;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PrevoziNudimFragment extends Fragment implements PrevozAdapter.PrevozAdapterOnClickHandler,  LoaderManager.LoaderCallbacks<String> {




    private PrevozAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mProggresBar;
    private String mSearchStartLocation="";
    private String mSearchEndLocation="";
    private static int SEARCH_NUDIM_PREVOZ=1;
    private static String ON_SCROLL_KEY="scroll_key";
    private static String PREVOZ_KEY="prevoz_key";
    private int mPage;
    private EndlessRecyclerViewScrollListener scrollListener;

    private SwipeRefreshLayout mSwipeRefreshRecycleView;



    private static final String SEARCH_QUERY_RESULT_KEY = "search_url_result";

    private View view;
    private static final int LOADER_ID = 70;


    public PrevoziNudimFragment() {

    }

    public static PrevoziNudimFragment  newInstance(MyRunnablePar onScroll) {
        PrevoziNudimFragment fragment = new PrevoziNudimFragment();
        Bundle arg=new Bundle();
        arg.putParcelable(ON_SCROLL_KEY,onScroll);
        fragment.setArguments(arg);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_prevozi,container,false);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view_prevozi);
        mSwipeRefreshRecycleView= (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_recycleview);
        mProggresBar = (ProgressBar) view.findViewById(R.id.proggres_bar);


        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new StartOffsetItemDecoration(10));
        mRecyclerView.addItemDecoration(new EndOffsetItemDecoration(10));





        return view;

    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int recyclerViewOrientation = LinearLayoutManager.VERTICAL;

        boolean shouldReverseLayout = false;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), recyclerViewOrientation, shouldReverseLayout);
        mRecyclerView.setLayoutManager(layoutManager);
        MyRunnablePar onScroll =null;
        Bundle arg=getArguments();
        if(arg!=null){
            if(arg.containsKey(ON_SCROLL_KEY)){
                onScroll= (MyRunnablePar) arg.getParcelable(ON_SCROLL_KEY);
            }

        }
        scrollListener=new EndlessRecyclerViewScrollListener(layoutManager,onScroll){
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                mPage = page;
                doSearchData();

            }
        };

        mRecyclerView.addOnScrollListener(scrollListener);

        mAdapter = new PrevozAdapter(this);

     if (savedInstanceState != null) {

            List<PrevozVM> prevozi = (List<PrevozVM>) savedInstanceState.getSerializable(SEARCH_QUERY_RESULT_KEY);
            if (prevozi != null) {

                mAdapter.setPrevozData(prevozi);

                mRecyclerView.setAdapter(mAdapter);

            }
        }
        else {
         doSearchData();
     }
        mSwipeRefreshRecycleView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mPage=0;
                scrollListener.resetState();
                doSearchData();

            }

        });
        mSwipeRefreshRecycleView.setColorSchemeResources(R.color.colorAccent);





    }

    private void doSearchData() {


        if (getLoaderManager().getLoader(LOADER_ID) == null) {
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            getLoaderManager().restartLoader(LOADER_ID, null, this);

        }

    }


    public void doSetSearchQuery(String searchStartLocation,String searchEndLocation){
        mPage=0;
        mSearchStartLocation=searchStartLocation;
        mSearchEndLocation=searchEndLocation;
        scrollListener.resetState();
        doSearchData();


    }




    public void doShowErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        Snackbar.make(view, getString(R.string.error_message), Snackbar.LENGTH_INDEFINITE).
                setAction(getString(R.string.refresh), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        doSearchData();
                    }
                }).setActionTextColor(Color.RED)
                .show();

    }
    public void doShowSearchNotFound() {

        /*Snackbar.make(view, getString(R.string.message_search_not_found), Snackbar.LENGTH_SHORT).
                show();
                */
    }
    public void doShowData() {


        mRecyclerView.setVisibility(View.VISIBLE);

    }

    @Override
    public void OnClick(PrevozVM prevoz) {
        Intent intent=new Intent(MyApp.getContext(),PrevozDetaljiActivity.class);
        Bundle arg=new Bundle();
        arg.putSerializable(PREVOZ_KEY,prevoz);
        intent.putExtras(arg);

        startActivity(intent);

    }


    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(getActivity()) {
            String mPrevozi;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                //   if (args == null) {
                //        return;
                // }


                if (mPrevozi != null) {

                    deliverResult(mPrevozi);
                }
                else{
                    forceLoad();
                }
            }

            @Override
            public String loadInBackground() {
                String jsonResult = null;


                try {
                    jsonResult = NetworkUtils.getResponseFromHttpUrl(NetworkUtils.buildSearchURL(mSearchStartLocation, mSearchEndLocation,SEARCH_NUDIM_PREVOZ, mPage));

                } catch (IOException e) {
                    e.printStackTrace();
                  jsonResult=null;
                }
                return jsonResult;

            }

            @Override
            public void deliverResult(String data) {
                mPrevozi = data;
                super.deliverResult(mPrevozi);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        mSwipeRefreshRecycleView.setRefreshing(false);
        if (data != null) {
            List<PrevozVM> mPrevozi = new ArrayList<>();
            Type listType = new TypeToken<ArrayList<PrevozVM>>() {
            }.getType();
            mPrevozi = GsonConverter.JsonToListArray(data, listType);
            if(mPage==0){
                mAdapter.setPrevozData(mPrevozi);
                mRecyclerView.setAdapter(mAdapter);
            }
            else {
                mAdapter.addPrevozDataa(mPrevozi);
            }

            if(mPrevozi.size()==0){
                doShowSearchNotFound();
            }
            else{
                doShowData();
            }


        } else {

            doShowErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}