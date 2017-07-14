package com.basic.prevoz.Services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.basic.prevoz.Activitys.ChatActivity;
import com.basic.prevoz.Helper.MyApp;
import com.basic.prevoz.Helper.MyRunnable;
import com.basic.prevoz.Models.ChatAdapter;
import com.basic.prevoz.Utils.NetworkUtils;
import com.basic.prevoz.Utils.Sesija;

import java.io.IOException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Developer on 07.07.2017..
 */

public class ChatMessageService extends Service {
    private Timer timer;
    private static final long UPDATE_INTERVAL = 2000;
    private static final long DELAY_INTERVAL = 100;
    private static Intent intent;
    private static MyRunnable<String> onNewMessage;
    private static boolean isRunning;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

timer=new Timer();
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
       timer.scheduleAtFixedRate(
               new TimerTask() {
                   @Override
                   public void run() {
                       if(Sesija.GetSignInUser()!=null||Sesija.getCurrentChatUserID()!=0){
                        doGetData();
                       }
                       else{
                           cancel();
                       }
                   }
               },DELAY_INTERVAL,UPDATE_INTERVAL
       );


        return super.onStartCommand(intent, flags, startId);
}

    private void doGetData() {

        new AsyncTask<URL, Void, String>() {
            @Override
            protected String doInBackground(URL... params) {
                String result="";
                try {
                    result= NetworkUtils.getResponseFromHttpUrl(params[0]);
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
                    onNewMessage.run(result);
                    Log.d("Service", "Chat Service, new message");
                }
            }
        }.execute(NetworkUtils.buildGetNewMessagesURL(Sesija.GetSignInUser().Id,Sesija.getCurrentChatUserID()));

    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {

    }

    public static void Start(int korisnikId, MyRunnable<String> newMessage){

        if(!isRunning) {
            isRunning = true;

            Sesija.setCurrentChatUserId(korisnikId);
            onNewMessage=newMessage;
            intent = new Intent(MyApp.getContext(), ChatMessageService.class);
            MyApp.getContext().startService(intent);
            Log.d("Service", "Chat Service started");
        }
    }
    public static void Stop(){

        if(isRunning) {
            isRunning = false;
            Sesija.setCurrentChatUserId(0);
            MyApp.getContext().stopService(intent);
            Log.d("Service", "Chat Service stopped");
        }
    }
}
