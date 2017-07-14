package com.basic.prevoz;

import android.app.ProgressDialog;
import android.content.Intent;

import android.net.Uri;
import android.os.AsyncTask;

import android.os.Bundle;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.prevoz.Helper.GoogleApi;

import com.basic.prevoz.Helper.GsonConverter;
import com.basic.prevoz.Helper.MyRunnable;
import com.basic.prevoz.Models.KorisniciVM;
import com.basic.prevoz.Utils.FacebookApiUtils;
import com.basic.prevoz.Utils.NetworkUtils;
import com.basic.prevoz.Utils.Sesija;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;


public class LoginActivity extends AppCompatActivity {



    private static String EMAIL="email";
    private static String SOURCE="source";
    private static String ID="id";
    private static String COVER="cover";
    private static String NAME="name";
    private static String PREVOZI_TOPIC="prevozi";

    private static String ACCESS_TOKEN="access_token";

    private static int RC_SIGN_IN=21;
    CallbackManager callbackManager;
    private ProgressDialog mProgressDialog;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {



        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(com.basic.prevoz.R.layout.activity_login);
mProgressDialog=new ProgressDialog(this);


        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        final LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
     /*TODO Napravit da pokupi email*/
        loginButton.setReadPermissions(EMAIL);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {

                GraphRequest graphRequest= GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        try {
                            object.put(ACCESS_TOKEN,loginResult.getAccessToken().getToken());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        doSaveFacebookAccountToSesija(object, new MyRunnable<KorisniciVM>() {
                            @Override
                            public void run(KorisniciVM korisnik) {


                                doSaveKorisnikToDB(korisnik);
                            }
                        });

                    }
                });
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {

                ShowLoginError();


            }
        });



        mGoogleApiClient=  GoogleApi.createClient(this);



        SignInButton signInButton = (SignInButton) findViewById(com.basic.prevoz.R.id.sign_in_button);

        setGoogleButtonTextAllCapsAndCustomText(signInButton,true);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();
            }
        });

    }


    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        }
        else{

//Facebook login result
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void handleGoogleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();


            KorisniciVM korisnik=GoogleAccToKorisnikVM(acct);
            doSaveKorisnikToDB(korisnik);


        }
        else {
           ShowLoginError();

        }
    }
    public void ShowLoginError(){
        Toast.makeText(this, getString(R.string.error_message), Toast.LENGTH_LONG).show();
    }

public void doLoginSuccess(){
    Toast.makeText(this,  getString(com.basic.prevoz.R.string.message_welcome)+" " + Sesija.GetSignInUser().ImePrezime, Toast.LENGTH_LONG).show();

    FirebaseMessaging.getInstance().subscribeToTopic(PREVOZI_TOPIC);

    Intent intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);

    startActivity(intent);

    finish();
}


    public KorisniciVM GoogleAccToKorisnikVM(GoogleSignInAccount acct) {
        final KorisniciVM korisnik = new KorisniciVM();
        korisnik.ImePrezime = acct.getDisplayName();
        korisnik.FirebaseToken=FirebaseInstanceId.getInstance().getToken();
        korisnik.Email = acct.getEmail();
        korisnik.UserId=acct.getId();
        if(acct.getPhotoUrl()!=null) {
            korisnik.photoUrl = acct.getPhotoUrl().toString();
        }
        if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
            Person person = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            if(person.hasCover()){
                korisnik.coverPhotoUrl=person.getCover().getCoverPhoto().getUrl();



            }
        }
        return korisnik;



    }

    public void doSaveFacebookAccountToSesija(JSONObject acct, final MyRunnable<KorisniciVM> onSuccess) {
        final KorisniciVM korisnik = new KorisniciVM();
        try {


            final String id=acct.getString(ID);

                korisnik.photoUrl = "https://graph.facebook.com/"+ id + "/picture?type=large";


            korisnik.UserId = id;
            korisnik.FirebaseToken=FirebaseInstanceId.getInstance().getToken();
            if (acct.has(NAME))
                korisnik.ImePrezime=acct.getString(NAME);

            if (acct.has(EMAIL))
                korisnik.Email=acct.getString(EMAIL);
            else{
                korisnik.Email="";

            }

                if (acct.has(ACCESS_TOKEN)) {
                    final String access_token = acct.getString(ACCESS_TOKEN);

                    new AsyncTask<URL, Void, String>() {
                        @Override
                        protected String doInBackground(URL... params) {
                        String result="";


                            try {

                                result=NetworkUtils.getResponseFromHttpUrl(params[0]);
                            } catch (IOException e) {
                                result=null;
                                e.printStackTrace();
                            }
                            return result;
                        }

                        @Override
                        protected void onPostExecute(String result) {
                            super.onPostExecute(result);

                            if(result!=null){
                                JSONObject coverImageJson;

                                try {
                                    coverImageJson =new JSONObject(result);
                                } catch (JSONException e) {

                                    e.printStackTrace();
                                    return;
                                }
                                       if(coverImageJson.has(COVER)){

                                    try {
                                        JSONObject cover=coverImageJson.getJSONObject(COVER);
                                        if(cover.has(SOURCE)){

                                            korisnik.coverPhotoUrl=cover.getString(SOURCE);


                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            onSuccess.run(korisnik);
                        }
                    }.execute(FacebookApiUtils.buildGraphFacebookCoverURL(id,access_token));
                }



        }
        catch(JSONException e) {
            e.printStackTrace();
        }




    }



    private void doSaveKorisnikToDB(final KorisniciVM korisnik) {
    mProgressDialog.setTitle(R.string.app_name);
    mProgressDialog.show();

        new AsyncTask<URL, Void, String>() {

            @Override
            protected String doInBackground(URL... params) {
                String result="";
                try {
                   result= NetworkUtils.postResponseToHttpUrl(params[0],GsonConverter.ObjectToJson(korisnik));
                } catch (IOException e) {
                    e.printStackTrace();
                    result=null;
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if(result!=null){

                    KorisniciVM korisnikDB=GsonConverter.JsonToObject(result,KorisniciVM.class);
                    Sesija.SaveSignInUser(korisnikDB);
                    doLoginSuccess();
                    mProgressDialog.dismiss();
                }
                else{

/*TODO Error da ispise*/
                }

            }
        }.execute(NetworkUtils.buildPostKorisnikURL());

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


    public static void setGoogleButtonTextAllCapsAndCustomText(SignInButton signInButton, boolean allCaps)
    {
        for (int i = 0; i < signInButton.getChildCount(); i++)
        {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView)
            {
                TextView tv = (TextView) v;
                tv.setAllCaps(allCaps);
                tv.setPadding(20, 0, 20, 0);

                return;
            }
        }
    }




}
