package com.basic.prevoz.Firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.basic.prevoz.Activitys.ChatActivity;
import com.basic.prevoz.Activitys.PrijateljiActivity;
import com.basic.prevoz.Activitys.ProfilActivity;
import com.basic.prevoz.Fragments.PrevoziTrazimFragment;
import com.basic.prevoz.Helper.GsonConverter;
import com.basic.prevoz.Helper.MyApp;
import com.basic.prevoz.Helper.MyRunnable;
import com.basic.prevoz.LoginActivity;
import com.basic.prevoz.MainActivity;
import com.basic.prevoz.Models.NotificationTextVM;
import com.basic.prevoz.Models.NotificationVM;
import com.basic.prevoz.R;
import com.basic.prevoz.Utils.NetworkUtils;
import com.basic.prevoz.Utils.Sesija;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.net.URL;

/**
 * Created by Developer on 19.06.2017..
 */

public class NotificationFirebaseMessageService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMsgService";
    private static String USER_KEY="user_key";
    public static int NOVI_PREVOZ=0;
    public static int ZAHTJEV_ZA_PRIJATELJSTVO=1;
    public static int PRIHVACEN_ZAHTJEV_ZA_PRIJATELJSTVO=2;
    public static int ZAHTJEV_ZA_PREVOZ=3;
    public static int NOVA_PORUKA=4;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {



        Log.d(TAG, "From: "+remoteMessage.getFrom());

        if(remoteMessage.getData().size()>0) {


            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            ///TODO* Check if data needs to be processed by long running job */ true) {
            // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
            // scheduleJob();
            //if(false){


            //}
            //else{


            //}
            //
        }
        if (remoteMessage.getNotification() != null) {

            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());



            sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(),null,R.drawable.ic_stat_name, null);
        }
        if(remoteMessage.getData()!=null) {
            if (remoteMessage.getData().containsKey("type")) {
                setNotificationData(Integer.parseInt(remoteMessage.getData().get("korisnikId")), remoteMessage.getData().get("imeKorisnika"), remoteMessage.getData().get("type"), remoteMessage.getData().get("photoUrl"), new MyRunnable<NotificationVM>() {
                    @Override
                    public void run(NotificationVM notification) {
                        sendNotification(notification.Text,notification.Title,notification.largImage,notification.smallIcon, notification.Intent);
                    }
                });
            }

        }
    }



    private void  setNotificationData(int korisnikId,String imeKorisnika, String type, final String photoUrl, final MyRunnable<NotificationVM> onSuccess){
        Intent intent;
   final NotificationVM notification=new NotificationVM();
     notification.smallIcon=R.drawable.ic_stat_name;
        if(NOVI_PREVOZ==Integer.valueOf(type)){
            notification.Intent=new Intent(this, MainActivity.class);
            notification.Text=getString(R.string.notification_text_new_drive);
            notification.Title=imeKorisnika;
            notification.smallIcon=R.drawable.ic_drive_notification_icon;
        }
        else  if(ZAHTJEV_ZA_PRIJATELJSTVO==Integer.valueOf(type)){
            notification.Intent=new Intent(this, ProfilActivity.class);
            notification.Intent.putExtra(USER_KEY,korisnikId);
           notification.Text=getString(R.string.notification_text_new_friend_request);
           notification.Title=imeKorisnika;
            notification.smallIcon=R.drawable.ic_person_add_black_24dp;
        }
        else   if(ZAHTJEV_ZA_PREVOZ==Integer.valueOf(type)){
            notification.Intent=new Intent(this, MainActivity.class);
           notification.Text=getString(R.string.notification_text_drive_request);
           notification.Title=imeKorisnika;
            notification.smallIcon=R.drawable.ic_drive_notification_icon;
        }
        else   if(PRIHVACEN_ZAHTJEV_ZA_PRIJATELJSTVO==Integer.valueOf(type)){
            notification.Intent=new Intent(this, ProfilActivity.class);
            notification.Intent.putExtra(USER_KEY,korisnikId);
           notification.Text=getString(R.string.notification_text_accepted_friend_request);
           notification.Title=imeKorisnika;
            notification.smallIcon=R.drawable.ic_people_notification_icon;
        }
        else    if(NOVA_PORUKA==Integer.valueOf(type)){

            if(korisnikId== Sesija.getCurrentChatUserID()){
               return;
            }
            notification.Intent=new Intent(this, ChatActivity.class);
            notification.Intent.putExtra(USER_KEY,korisnikId);
           notification.Text=getString(R.string.notification_text_new_message);
           notification.Title=imeKorisnika;
            notification.smallIcon=R.drawable.ic_menu_message_24dp;
        }




        new AsyncTask<Uri, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Uri... params) {
                try {
                    return NetworkUtils.getBitmapImageFromUri(params[0]);
                } catch (Exception e) {
                    e.printStackTrace();

                    return  null;
                }

            }
            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                notification.largImage=bitmap;
                onSuccess.run(notification);
            }

        }.execute(Uri.parse(photoUrl).buildUpon().build());






    }




    private void  sendNotification(String messageText, String messageTitle,Bitmap largeIcon,int smallIcon, Intent intent){




        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(largeIcon)
                .setSmallIcon(smallIcon)
                .setContentText(messageText)
                .setContentTitle(messageTitle)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);



        NotificationManager notificationManager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());

    }



}

