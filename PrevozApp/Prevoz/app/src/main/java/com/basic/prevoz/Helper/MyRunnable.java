package com.basic.prevoz.Helper;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Developer on 15.06.2017..
 */

public interface MyRunnable <T> extends  Serializable {
   void run(T t);
}
