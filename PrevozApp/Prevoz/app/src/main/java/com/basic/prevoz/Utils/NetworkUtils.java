package com.basic.prevoz.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.SearchView;
import android.util.Log;

import com.basic.prevoz.Models.KorisniciVM;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Developer on 29.04.2017..
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static String BASIC_URL = "http://prevoz-001-site1.htempurl.com/";
    private static String LOCAL_URL = "http://192.168.1.3:5408";
    private static String CONTROLER_PREVOZI = "Prevozi";
    private static String CONTROLER_ZAHTJEV_ZA_PREVOZ = "ZahtjevZaPrevoz";
    private  static String CONTROLER_KORISNICI="Korisnici";
    private  static String CONTROLER_MESSAGE="Message";
    private  static String CONTROLER_CHAT="Chat";

    private  static String CONTROLER_PRIJATELJI="Prijatelji";
    private static String ACTION_PRETRAGA = "Pretraga";
    private static String ACTION_DELETE_TOKEN = "DeleteToken";
    private static String ID = "id";
    private static String PAGE="page";
    private static String KOLICINA="kolicina";
    private static String KORISNIK_ID="korisnikid";
    private static String KORISNIK1_ID="korisnik1id";
    private static String KORISNIK2_ID="korisnik2id";
    private static String ACTION_POSALJI = "Posalji";
    private static String ACTION_DODAJ = "Dodaj";
    private static String  ACTION_NEPROCITANE ="Neprocitane";
    private static String ACTION_PRIHVATI = "PRIHVATI";
    private static String ACTION_IZBRISI = "IZBRISI";

    private static String ACTION_DETALJI = "Detalji";
    private static String QUERY = "q";
    private static String STATUS = "Status";
    private static String ACTION_IZBRISI_TOKEN = "IzbrisiToken";
    private static String START = "startGrad";
    private static String DATUM = "datum";
    private static String KRAJ = "krajGrad";
    private static String TIP_PREVOZA = "tipPrevoza";


    public static URL buildSearchURL(String start, String kraj, String datum, int tipPrevoza, int page) {
        Uri uri = Uri.parse(BASIC_URL).buildUpon().
                appendPath(CONTROLER_PREVOZI).
                appendPath(ACTION_PRETRAGA).
                appendQueryParameter(START, start).
                appendQueryParameter(KRAJ, kraj).
                appendQueryParameter(DATUM,datum).
                appendQueryParameter(TIP_PREVOZA,String.valueOf(tipPrevoza)).
                appendQueryParameter(PAGE,String.valueOf(page)).
                build();


        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built url" + url.toString());
        return url;


    }
    public static URL buildSendDriveRequestURL() {
        Uri uri = Uri.parse(BASIC_URL).buildUpon().
                appendPath(CONTROLER_ZAHTJEV_ZA_PREVOZ).
                appendPath(ACTION_POSALJI).

                build();


        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built url" + url.toString());
        return url;


    }


    public static URL buildSearchUserFriendsURL(int korisnikId,String q,int Status){

        Uri uri=Uri.parse(BASIC_URL).buildUpon().
                appendPath(CONTROLER_PRIJATELJI).
                appendPath(ACTION_PRETRAGA).
                appendQueryParameter(ID,String.valueOf(korisnikId)).
                appendQueryParameter(QUERY,q).
        appendQueryParameter(STATUS,String.valueOf(Status))
                .build();

        URL url=null;
        try {
            url=new  URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built url" + url.toString());
        return url;
    }

    public static URL buildSendFriendRequestURL(){

        Uri uri=Uri.parse(BASIC_URL).buildUpon().
                appendPath(CONTROLER_PRIJATELJI).
                appendPath(ACTION_DODAJ).build();


        URL url=null;
        try {
            url=new  URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built url" + url.toString());
        return url;
    }
    public static URL buildAcceptFriendRequestURL(int id){

        Uri uri=Uri.parse(BASIC_URL).buildUpon().
                appendPath(CONTROLER_PRIJATELJI).
                appendPath(ACTION_PRIHVATI).
                appendQueryParameter(ID,String.valueOf(id))
                .build();


        URL url=null;
        try {
            url=new  URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built url" + url.toString());
        return url;
    }
    public static URL buildDeleteFriendRequestURL(int id){

        Uri uri=Uri.parse(BASIC_URL).buildUpon().
                appendPath(CONTROLER_PRIJATELJI).
                appendPath(ACTION_IZBRISI).
                appendQueryParameter(ID,String.valueOf(id))
                .build();


        URL url=null;
        try {
            url=new  URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built url" + url.toString());
        return url;
    }
    public static URL buildGetDetaljiKorisnikaURL(int id){

        Uri uri=Uri.parse(BASIC_URL).buildUpon().
                appendPath(CONTROLER_KORISNICI).
                appendPath(ACTION_DETALJI).
                appendQueryParameter(ID,String.valueOf(id)).
                appendQueryParameter(KORISNIK_ID,String.valueOf(Sesija.GetSignInUser().Id))
                .build();


        URL url=null;
        try {
            url=new  URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built url" + url.toString());
        return url;
    }


    public static URL buildSearchUserMessagesURL(int korisnikId,String q){

        Uri uri=Uri.parse(BASIC_URL).buildUpon().
                appendPath(CONTROLER_CHAT).
                appendPath(ACTION_PRETRAGA).
                appendQueryParameter(ID,String.valueOf(korisnikId)).
                appendQueryParameter(QUERY,q)
                .build();

        URL url=null;
        try {
            url=new  URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built url" + url.toString());
        return url;
    }
    public static URL buildGetConversation(int korisnik1Id,int korisnik2Id, int page){

        Uri uri=Uri.parse(BASIC_URL).buildUpon().
                appendPath(CONTROLER_CHAT).
                appendPath(ACTION_PRETRAGA).
                appendQueryParameter(KORISNIK1_ID,String.valueOf(korisnik1Id)).
                appendQueryParameter(KORISNIK2_ID,String.valueOf(korisnik2Id)).
                appendQueryParameter(PAGE,String.valueOf(page))
                .build();

        URL url=null;
        try {
            url=new  URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built url" + url.toString());
        return url;
    }


    public static URL buildPostPrevozURL() {

                Uri uri = Uri.parse(BASIC_URL).buildUpon().
                appendPath(CONTROLER_PREVOZI).appendPath(ACTION_DODAJ).
                build();


        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built url" + url.toString());
        return url;


    }
    public static URL buildSendMessageURL() {

        Uri uri = Uri.parse(BASIC_URL).buildUpon().
                appendPath(CONTROLER_CHAT).appendPath(ACTION_POSALJI).
                build();


        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built url" + url.toString());
        return url;


    }

    public static URL buildGetNewMessagesURL(int korisnik1Id, int korisnik2Id) {

        Uri uri = Uri.parse(BASIC_URL).buildUpon().
                appendPath(CONTROLER_CHAT).appendPath(ACTION_NEPROCITANE).
                appendQueryParameter(KORISNIK1_ID,String.valueOf(korisnik1Id)).
                appendQueryParameter(KORISNIK2_ID,String.valueOf(korisnik2Id)).
                build();


        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built url" + url.toString());
        return url;


    }

    public static URL buildDeleteFirebaseTokenURL(int id) {

        Uri uri = Uri.parse(BASIC_URL).buildUpon().
                appendPath(CONTROLER_KORISNICI).appendPath(ACTION_DELETE_TOKEN).
                appendQueryParameter(ID,String.valueOf(id)).
                build();


        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built url" + url.toString());
        return url;


    }
    public static URL buildPostKorisnikURL() {

        Uri uri = Uri.parse(BASIC_URL).buildUpon().
                appendPath(CONTROLER_KORISNICI).appendPath(ACTION_DODAJ).

                build();


        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built url" + url.toString());
        return url;


    }

    public static URL buildDeleteUserTokenURL() {

        Uri uri = Uri.parse(BASIC_URL).buildUpon().
                appendPath(CONTROLER_KORISNICI).appendPath(ACTION_DODAJ).

                build();


        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "Built url" + url.toString());
        return url;


    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }




public static String postResponseToHttpUrl(URL url, String jsonParameters) throws IOException {
    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
    try {
        urlConnection.setDoOutput(true);
        urlConnection.setChunkedStreamingMode(0);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");
        Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));


        if (!jsonParameters.equals("")) {

            writer.write(jsonParameters);


            writer.close();
        }


        InputStream in = urlConnection.getInputStream();

        Scanner scanner = new Scanner(in);
        scanner.useDelimiter("\\A");

        boolean hasInput = scanner.hasNext();
        if (hasInput) {
            return scanner.next();
        } else {
            return null;
        }


    } finally {
        urlConnection.disconnect();
    }



}



    public static Bitmap getBitmapImageFromUri(Uri uri) throws Exception{
    try {
        URL url = new URL(uri.toString());
        InputStream in = url.openStream();
           ByteArrayOutputStream stream = new ByteArrayOutputStream();
         Bitmap image= BitmapFactory.decodeStream(in);
        image.compress(Bitmap.CompressFormat.PNG,100,stream);
        return image;

    } catch (Exception e) {
                    /* TODO log error */
    }
    return null;

}


}
