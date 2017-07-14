package com.basic.prevoz.Activitys.DodajNoviPrevoz;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.basic.prevoz.Helper.GsonConverter;
import com.basic.prevoz.Helper.MyApp;
import com.basic.prevoz.Models.Global;
import com.basic.prevoz.NavigationDrawerActivity;
import com.basic.prevoz.R;
import com.basic.prevoz.Utils.NetworkUtils;
import com.basic.prevoz.Utils.Sesija;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class NoviPrevozVrijemeActivity extends AppCompatActivity {


private ImageButton mVrijemeButton;
    private ImageButton mDatumButton;
    private TextView mVrijeme;
    private TextView mDatum;

    private int mYear, mMonth, mDay, mHour, mMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novi_prevoz_vrijeme);
        Toolbar mToolbar= (Toolbar) findViewById(R.id.toolbar_vrijeme);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mVrijeme= (TextView) findViewById(R.id.tv_vrijeme);
        mVrijemeButton= (ImageButton) findViewById(R.id.button_vrijeme);
        mVrijemeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOpenTimePickerDialog();
            }
        });
        mDatum= (TextView) findViewById(R.id.tv_datum);
        mDatumButton= (ImageButton) findViewById(R.id.button_datum);
        mDatumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOpenDatePickerDialog();
            }
        });

      FloatingActionButton mButtonNext= (FloatingActionButton) findViewById(R.id.button_next);
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doButtonNextClick();
            }
        });








        }

    private void doOpenDatePickerDialog() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


       new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mYear=year;
                        mMonth=monthOfYear;
                        mDay=dayOfMonth;
                        mDatum.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay).show();


    }

    private void doOpenTimePickerDialog() {


        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);


       new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        mHour=hourOfDay;
                        mMinute=minute;
                        mVrijeme.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false).show();
    }
    private void doButtonNextClick() {
        Global.noviPrevoz.Korisnik= Sesija.GetSignInUser();
        Global.noviPrevoz.Aktivno=true;
        Global.noviPrevoz.BrojMjesta=2;
        Global.noviPrevoz.Cijena=13;
        Calendar calendar = Calendar.getInstance();
        calendar.set(mYear,mMonth,mDay,mHour,mMinute);
        Global.noviPrevoz.DatumKretanja=calendar.getTime();
        Global.noviPrevoz.Opis="Trazim drustvo dosadno mi sam vozit";
        Global.noviPrevoz.PrevoznoSredstvoId=1;
        Global.noviPrevoz.SlobodnoMjesto=true;
        Global.noviPrevoz.Telefon="+38762551154";
        new AsyncTask<URL, Void, Void>() {
            @Override
            protected Void doInBackground(URL... params) {

                try {
                    NetworkUtils.postResponseToHttpUrl(params[0], GsonConverter.ObjectToJson(Global.noviPrevoz));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);


                Intent intent = new Intent(NoviPrevozVrijemeActivity.this, NavigationDrawerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                finish();
            }
        }.execute(NetworkUtils.buildPostPrevozURL());

    }


}




