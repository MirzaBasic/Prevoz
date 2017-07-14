package com.basic.prevoz.Models;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Developer on 27.06.2017..
 */

public class PorukeVM implements Serializable {


    public  int Id;
    public String Text;
    public int KorisnikPoslaoId;
    public int KorisnikPrimioId;

    public Date DatumKreiranja;
    public int Status;

    public  KorisniciVM Korisnik;
    public String DataType;
}
