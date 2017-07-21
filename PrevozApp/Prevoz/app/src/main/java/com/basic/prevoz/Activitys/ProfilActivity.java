package com.basic.prevoz.Activitys;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basic.prevoz.Firebase.NotificationFirebaseMessageService;
import com.basic.prevoz.Helper.GsonConverter;
import com.basic.prevoz.Helper.MyApp;
import com.basic.prevoz.Helper.MyRunnable;
import com.basic.prevoz.Models.NotificationTextVM;
import com.basic.prevoz.Models.KorisniciVM;
import com.basic.prevoz.Models.NotificationVM;
import com.basic.prevoz.Models.PrijateljiVM;
import com.basic.prevoz.R;
import com.basic.prevoz.Utils.NetworkUtils;
import com.basic.prevoz.Utils.Sesija;

import java.io.IOException;
import java.net.URL;

public class ProfilActivity extends AppCompatActivity {

    private static int PENDING = 0;
    private static int ACCEPTED = 1;
    private static int DECLINED = 2;
    private static int BLOCKED = 3;
private     AlertDialog.Builder mAlertDialogBuilder;

    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbar;;
    private KorisniciVM mKorisnik;
    private static String USER_KEY = "user_key";
    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_profil);
        mCollapsingToolbar= (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left_thick_white);
         mAlertDialogBuilder = new AlertDialog.Builder(this);

        mFloatingActionButton= (FloatingActionButton) findViewById(R.id.floating_button);

        if (getIntent().hasExtra(USER_KEY)) {
            int korisnikId = getIntent().getIntExtra(USER_KEY, 0);

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
                        doSetDetaljiKorisnika();
                        doSetFloatingActonBar();

                    }
                }
            }.execute(NetworkUtils.buildGetDetaljiKorisnikaURL(korisnikId));


        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void dialPhoneNumber() {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + mKorisnik.Telefon));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

private void doSetFloatingActonBar(){


    if (mKorisnik.Prijatelj == null) {
        setIsAddFriend();
    } else if (mKorisnik.Prijatelj.Status == 0) {
        if (mKorisnik.Prijatelj.PoslaoKorisnikId == Sesija.GetSignInUser().Id) {
            setIsPendingFriend();
        } else {

            setIsAcceptFriend();
        }

    } else if (mKorisnik.Prijatelj.Status == 1) {
        doIsFriend();

    }


}
    private void doSetDetaljiKorisnika() {
mCollapsingToolbar.setTitle(mKorisnik.ImePrezime);

        TextView mPhoneNumber= (TextView) findViewById(R.id.tv_broj_telefona);

            mPhoneNumber.setText("+38762075369");


            mPhoneNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialPhoneNumber();
                }
            });

            ImageView mCallUser = (ImageView) findViewById(R.id.button_image_call);
            mCallUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialPhoneNumber();
                }
            });

            TextView mSendMessageToUserTV = (TextView) findViewById(R.id.tv_privatna_poruka);
            mSendMessageToUserTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfilActivity.this, ChatActivity.class);

                    intent.putExtra(USER_KEY, mKorisnik.Id);
                    startActivity(intent);
                }
            });
            ImageView mSendMessageToUserIcon = (ImageView) findViewById(R.id.button_image_send_message);
            mSendMessageToUserIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfilActivity.this, ChatActivity.class);

                    intent.putExtra(USER_KEY, mKorisnik.Id);
                    startActivity(intent);

                }
            });
            TextView mEmail = (TextView) findViewById(R.id.tv_email);
            mEmail.setText(mKorisnik.Email);

            final ImageView mUserCoverImage = (ImageView) findViewById(R.id.image_user_cover);
            final ImageView mUserImage = (ImageView) findViewById(R.id.image_user);

            if (mKorisnik.coverPhotoUrl != null) {
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
                    protected void onPostExecute(Bitmap bitmap) {
                        super.onPostExecute(bitmap);

                        if (bitmap != null) {

                            mUserCoverImage.setImageBitmap(bitmap);

                        }
                    }
                }.execute(Uri.parse(mKorisnik.coverPhotoUrl).buildUpon().build());
            }
            if (mKorisnik.photoUrl != null) {
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
                    protected void onPostExecute(Bitmap bitmap) {
                        super.onPostExecute(bitmap);

                        if (bitmap != null) {

                            mUserImage.setImageBitmap(bitmap);

                        }
                    }
                }.execute(Uri.parse(mKorisnik.photoUrl).buildUpon().build());
            }


    }

    private void doAcceptFriendRequest(final MyRunnable<Boolean> onSuccess) {

        new AsyncTask<URL, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(URL... params) {
                boolean success = true;

                try {
                    NetworkUtils.postResponseToHttpUrl(params[0], "");
                } catch (IOException e) {
                    e.printStackTrace();
                    success = false;
                }
                return success;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);

                onSuccess.run(success);

            }
        }.execute(NetworkUtils.buildAcceptFriendRequestURL(mKorisnik.Prijatelj.Id));
    }

    private void doDeleteFriendRequest(final MyRunnable<Boolean> onSuccess) {

        new AsyncTask<URL, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(URL... params) {
                boolean success = true;

                try {
                    NetworkUtils.getResponseFromHttpUrl(params[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                    success = false;
                }
                return success;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                super.onPostExecute(success);

                onSuccess.run(success);

            }
        }.execute(NetworkUtils.buildDeleteFriendRequestURL(mKorisnik.Prijatelj.Id));
    }

    public void doIsFriend() {

  mFloatingActionButton.setImageResource(R.drawable.ic_people_black_100dp);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                 mAlertDialogBuilder.setIcon(R.drawable.ic_delete_black_24dp);

                mAlertDialogBuilder.setTitle(R.string.title_delete_from_friends);

                mAlertDialogBuilder.setPositiveButton(R.string.izbrisi, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int id) {

                        doDeleteFriendRequest(new MyRunnable<Boolean>() {
                            @Override
                            public void run(Boolean success) {
                                if (success) {
                                    Snackbar.make(v, getString(R.string.friend_deleted_successful), Snackbar.LENGTH_LONG)
                                            .show();
                                    mKorisnik.Prijatelj = null;
                                    setIsAddFriend();

                                } else {
                                    Snackbar.make(v, getString(R.string.friend_request_deleted_error), Snackbar.LENGTH_LONG)
                                            .show();

                                }
                            }
                        });
                    }
                });
                mAlertDialogBuilder.setNegativeButton(R.string.odustani, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog dialog = mAlertDialogBuilder.create();
                dialog.show();
            }
        });


    }

    public void setIsAcceptFriend() {

      mFloatingActionButton.setImageResource(R.drawable.ic_account_check);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                doAcceptFriendRequest(new MyRunnable<Boolean>() {
                    @Override
                    public void run(Boolean success) {
                        if (success) {


                            Snackbar.make(v, getString(R.string.friend_request_accepted_successful), Snackbar.LENGTH_LONG)
                                    .show();
                            mKorisnik.Prijatelj.Status = ACCEPTED;
                            doIsFriend();

                        } else {
                            Snackbar.make(v, getString(R.string.friend_request_accepted_error), Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
            }
        });





    }

    public void setIsPendingFriend() {

      mFloatingActionButton.setImageResource(R.drawable.ic_person_outline_black_100dp);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                doDeleteFriendRequest(new MyRunnable<Boolean>() {
                    @Override
                    public void run(Boolean success) {
                        if (success) {
                            mKorisnik.Prijatelj = null;
                            setIsAddFriend();

                        } else {
                            Snackbar.make(v, getString(R.string.friend_request_deleted_error), Snackbar.LENGTH_LONG)
                                    .show();

                        }
                    }
                });
            }
        });


    }

    public void setIsDeleteFriend() {


    }

    public void setIsAddFriend() {

       mFloatingActionButton.setImageResource(R.drawable.ic_person_add_black_24dp);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                doSendFriendRequest(new MyRunnable<String>() {
                    @Override
                    public void run(String result) {
                        if (result != null) {



                            Snackbar.make(v, getString(R.string.friend_request_send_successful), Snackbar.LENGTH_LONG)
                                    .show();
                            mKorisnik.Prijatelj = GsonConverter.JsonToObject(result, PrijateljiVM.class);
                            setIsPendingFriend();



                        } else {
                            Snackbar.make(v, getString(R.string.friend_request_send_error), Snackbar.LENGTH_LONG)
                                    .show();
                            mKorisnik.Prijatelj = null;
                        }
                    }
                });
            }
        });

    }


    private void doSendFriendRequest(final MyRunnable<String> onSuccess) {
        mKorisnik.Prijatelj = new PrijateljiVM();
        mKorisnik.Prijatelj.Korisnik1Id = Sesija.GetSignInUser().Id;
        mKorisnik.Prijatelj.Korisnik2Id = mKorisnik.Id;
        mKorisnik.Prijatelj.PoslaoKorisnikId = Sesija.GetSignInUser().Id;

        new AsyncTask<URL, Void, String>() {
            @Override
            protected String doInBackground(URL... params) {
                String result = "";

                try {
                    result = NetworkUtils.postResponseToHttpUrl(params[0], GsonConverter.ObjectToJson(mKorisnik.Prijatelj));
                } catch (IOException e) {
                    e.printStackTrace();
                    result = null;
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                onSuccess.run(result);

            }
        }.execute(NetworkUtils.buildSendFriendRequestURL());
    }




}