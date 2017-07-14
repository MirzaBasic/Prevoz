package com.basic.prevoz.Models;

import java.util.Date;

/**
 * Created by Developer on 14.07.2017..
 */

public class ZahtjevZaPrevozVM {
    public int Id ;

    public int KorisnikId ;

    public  KorisniciVM Korisnik ;

    public int Status ;
    public int Kolicina;

    public Date DatumKreiranja ;

    public int PrevozId ;

    public  PrevozVM Prevoz ;
}
