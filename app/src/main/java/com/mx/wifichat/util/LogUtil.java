package com.mx.wifichat.util;

import android.util.Log;

/**
 * Created by MX on 2015/6/14.
 */
public class LogUtil {
    public static boolean LOG = true;

    public static void v(String tag, String msg) {
        if (LOG)
            Log.v(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (LOG)
            Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (LOG)
            Log.i(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (LOG)
            Log.w(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (LOG)
            Log.e(tag, msg);
    }

    public static void wtf(String tag, String msg) {
        if (LOG)
            Log.wtf(tag, msg);
    }

    public static void wtf(String tag, Throwable tr) {
        if (LOG)
            Log.wtf(tag, tr);
    }
}
