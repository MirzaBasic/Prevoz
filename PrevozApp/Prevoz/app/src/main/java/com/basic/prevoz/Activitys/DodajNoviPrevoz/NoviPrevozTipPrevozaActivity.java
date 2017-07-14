package com.basic.prevoz.Activitys.DodajNoviPrevoz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.basic.prevoz.Models.Global;
import com.basic.prevoz.Models.PrevozVM;
import com.basic.prevoz.R;

public class NoviPrevozTipPrevozaActivity extends AppCompatActivity {

    private static  int NUDIM=1;
    private static  int TRAZIM=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novi_prevoz_tip_prevoza);
        Toolbar mToolbar= (Toolbar) findViewById(R.id.toolbar_tip_prevoza);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageButton mTrazimButton= (ImageButton) findViewById(R.id.button_trazim);
        ImageButton mNudimButton= (ImageButton) findViewById(R.id.button_nudim);

        mTrazimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(NoviPrevozTipPrevozaActivity.this,NoviPrevozStaniceActivity.class);
                if(intent.resolveActivity(getPackageManager())!=null){

                    Global.noviPrevoz.TipPrevoza=TRAZIM;
                    startActivity(intent);


                }
            }
        });

        mNudimButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(NoviPrevozTipPrevozaActivity.this,NoviPrevozStaniceActivity.class);
                if(intent.resolveActivity(getPackageManager())!=null){
                    Global.noviPrevoz.TipPrevoza=NUDIM;
                    startActivity(intent);

                }
            }
        });
    }
}
