package com.basic.prevoz.Utils;

import android.app.Activity;
import android.content.SharedPreferences;

import com.basic.prevoz.Helper.GsonConverter;
import com.basic.prevoz.Helper.MyApp;
import com.basic.prevoz.Models.KorisniciVM;

/**
 * Created by Developer on 23.05.2017..
 */

public class Sesija {
    public static String USER_DETAILS_SHARED_PREFERENCE_KEY="user_details_key";
    public static String CHAT_USER_ID_SHARED_PREFERENCE_KEY="chat_user_id_key";
    private static final String PREFS_NAME = "SharedPreferencesFile";




    public static void SaveSignInUser(KorisniciVM signInAccount){
        String jsonUser= GsonConverter.ObjectToJson(signInAccount);
        SharedPreferences preferences= MyApp.getContext().getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString(USER_DETAILS_SHARED_PREFERENCE_KEY,jsonUser);
        editor.commit();

    }

    public static KorisniciVM GetSignInUser(){

        SharedPreferences preferences= MyApp.getContext().getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
   String jsonUser=preferences.getString(USER_DETAILS_SHARED_PREFERENCE_KEY,"");
        if(jsonUser.length()==0){
            return null;

        }

      return GsonConverter.JsonToObject(jsonUser,KorisniciVM.class);


    }

    public static void setCurrentChatUserId(int korinsnikId){
        SharedPreferences preferences=MyApp.getContext().getSharedPreferences(PREFS_NAME,Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt(CHAT_USER_ID_SHARED_PREFERENCE_KEY,korinsnikId);
        editor.commit();
    }
    public static int getCurrentChatUserID(){
        SharedPreferences preferences=MyApp.getContext().getSharedPreferences(PREFS_NAME,Activity.MODE_PRIVATE);
int korisnikId=preferences.getInt(CHAT_USER_ID_SHARED_PREFERENCE_KEY,0);
        return korisnikId;
    }
}
