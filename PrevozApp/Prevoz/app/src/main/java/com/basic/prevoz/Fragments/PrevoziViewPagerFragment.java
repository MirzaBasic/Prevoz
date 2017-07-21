package com.basic.prevoz.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.basic.prevoz.Activitys.DodajNoviPrevoz.NoviPrevozTipPrevozaActivity;
import com.basic.prevoz.Helper.MyAnimations;
import com.basic.prevoz.Helper.MyApp;
import com.basic.prevoz.Helper.MyRunnablePar;
import com.basic.prevoz.Models.Global;
import com.basic.prevoz.Models.PrevozVM;
import com.basic.prevoz.NavigationDrawerActivity;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.basic.prevoz.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class PrevoziViewPagerFragment extends Fragment {


    private static int SEARCH_BUTTON_START_LOCATION_ID = 1;
    private static int SEARCH_BUTTON_END_LOCATION_ID = 2;
    private static int SEARCH_BUTTON_DATE_ID = 3;
    private static int SEARCH_BUTTON_ID = 0;
    private int mYear, mMonth, mDay;
    private LinearLayout mSearchView;
    private Button mSearchStartLocation;
    private Button mSearchEndLocation;
    private Button mSearchDate;
    private static int PLACE_PICKER_REQUEST = 99;
    private SimpleAdapter mPagerAdapter;
    private View view;
    private NavigationDrawerActivity activity;


    private ViewPager mViewPager;
    private FloatingActionButton mFloatingButtonAddNew;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_prevozi_view_pager,container,false);
        activity=((NavigationDrawerActivity) getActivity());
        setHasOptionsMenu(true);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_prevozi);
        activity.setSupportActionBar(toolbar);

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburgere_24dp);
        MyRunnablePar onScroll=new MyRunnablePar() {


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {

            }

            @Override
            public void run(int dy) {
                if( dy>0) {

                    MyAnimations.hideFloatingButton(mFloatingButtonAddNew);
                }else {
                    MyAnimations.showFloatingButton(mFloatingButtonAddNew);
                }
            }
        };


        mViewPager = (ViewPager) view.findViewById(R.id.container);

        mPagerAdapter = new SimpleAdapter(getActivity().getSupportFragmentManager());
        mPagerAdapter.addFragment(PrevoziTrazimFragment.newInstance(onScroll),getString(R.string.title_viewpager_trazim));
        mPagerAdapter.addFragment(PrevoziNudimFragment.newInstance(onScroll),getString(R.string.title_viewpager_nudim));


        mViewPager.setAdapter(mPagerAdapter);
        mPagerAdapter.instantiateItem(mViewPager,0);


        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);




        mFloatingButtonAddNew = (FloatingActionButton) view.findViewById(R.id.floating_button_add_new_prevoz);
        mFloatingButtonAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Global.noviPrevoz=new PrevozVM();
               Intent intent =new Intent(getActivity(),NoviPrevozTipPrevozaActivity.class);
                if(intent.resolveActivity(getActivity().getPackageManager())!=null){

                    startActivity(intent);

                }

            }
        });




        mSearchView = (LinearLayout) toolbar.findViewById(R.id.search_layout);
        mSearchEndLocation = (Button) mSearchView.findViewById(R.id.searchview_end_location);
        mSearchStartLocation = (Button) mSearchView.findViewById(R.id.searchview_start_location);
        mSearchDate= (Button) mSearchView.findViewById(R.id.searchview_date);
        doListenOnButtonSearchClick();





        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.prevozi,menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id== android.R.id.home) { // This is the home/back button
            activity.OpenDrawer();
        }


          if(id== R.id.action_pretraga) {
            if (mSearchView.getVisibility() == View.GONE) {
                mSearchView.setVisibility(View.VISIBLE);

                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                item.setIcon(R.drawable.ic_close_white_24dp);
            } else {
                mSearchView.setVisibility(View.GONE);
                mSearchStartLocation.setText("");
                mSearchDate.setText("");
                mSearchEndLocation.setText("");
                mPagerAdapter.notifyDataSetChanged();
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                item.setIcon(R.drawable.ic_search_24dp);
            }

          }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.section_view_pager, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }


    private void doListenOnButtonSearchClick() {


        mSearchStartLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SEARCH_BUTTON_ID = SEARCH_BUTTON_START_LOCATION_ID;
                doCallPlacePicker();
            }
        });
        mSearchEndLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SEARCH_BUTTON_ID = SEARCH_BUTTON_END_LOCATION_ID;
                doCallPlacePicker();
            }
        });
        mSearchDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SEARCH_BUTTON_ID = SEARCH_BUTTON_DATE_ID;
                doCallDatePicker();
            }
        });

    }

    private void doCallDatePicker() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;
                        mSearchDate.setText((monthOfYear + 1) + "-" +dayOfMonth + "-" +  year);
                        mPagerAdapter.notifyDataSetChanged();
                    }
                }, mYear, mMonth, mDay).show();




    }

    private void doCallPlacePicker() {


        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(getActivity());

            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());

                if (SEARCH_BUTTON_ID == SEARCH_BUTTON_START_LOCATION_ID) {

                    mSearchStartLocation.setText(place.getName());
                    mPagerAdapter.notifyDataSetChanged();



                }
                if (SEARCH_BUTTON_ID == SEARCH_BUTTON_END_LOCATION_ID) {

                    mSearchEndLocation.setText(place.getName());
                    mPagerAdapter.notifyDataSetChanged();




                }}


            }
    }

    public class SimpleAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public SimpleAdapter( FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }

        @Override
        public int getItemPosition(Object object) {

            if(object instanceof PrevoziNudimFragment){

                PrevoziNudimFragment fragment=(PrevoziNudimFragment) object;
                fragment.doSetSearchQuery(mSearchStartLocation.getText().toString(),mSearchEndLocation.getText().toString(),mSearchDate.getText().toString());

            }
            if(object instanceof PrevoziTrazimFragment){

                PrevoziTrazimFragment fragment=(PrevoziTrazimFragment) object;
                fragment.doSetSearchQuery(mSearchStartLocation.getText().toString(),mSearchEndLocation.getText().toString(),mSearchDate.getText().toString());


            }
            return super.getItemPosition(object);
        }

    }





}