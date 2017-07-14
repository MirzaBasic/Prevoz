package com.basic.prevoz.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Developer on 23.06.2017..
 */

public class DateConverter {
    public static String to_dd_mm_yyyy(Date date)
    {
        return new SimpleDateFormat("MM.dd.yyyy").format(date.getTime());
    }
    public static String to_dd_mm_yyyy_hh_mm(Date date)
    {
        return new SimpleDateFormat("MM.dd.yyyy h:mm a").format(date);
    }
    public static String to_hh_mm(Date date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        String strDate= formatter.format(date);
        return strDate;
    }
}
