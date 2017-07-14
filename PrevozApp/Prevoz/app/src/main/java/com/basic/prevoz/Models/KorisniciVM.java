package com.basic.prevoz.Models;

import android.content.Intent;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Developer on 23.05.2017..
 */

public class KorisniciVM implements Serializable{


    public int Id ;
    public String UserId;
    public String Email ;
    public String ImePrezime;
    public String photoUrl;
    public String FirebaseToken;
    public int FirebaseTokenId;
    public PrijateljiVM Prijatelj;
    public String coverPhotoUrl;



    public String Telefon;
}
