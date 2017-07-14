package com.basic.prevoz.Models;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

/**
 * Created by Developer on 29.04.2017..
 */

public class PrevozVM implements Serializable {

    public int Id ;
    public String Opis ;
    public float Cijena ;
    public int BrojMjesta ;
    public boolean SlobodnoMjesto ;
    public boolean Aktivno ;


    public Date DatumKretanja ;
    public Date DatumKreiranja ;


    public int TipPrevoza ;

    public KorisniciVM Korisnik ;
    public int KorisnikId ;
    public List<StanicaVM> Stanice ;
    public int PrevoznoSredstvoId ;

    public String Telefon;
}
