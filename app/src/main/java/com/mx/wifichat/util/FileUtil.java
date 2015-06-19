package com.mx.wifichat.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by MX on 2015/6/14.
 */
public class FileUtil {
    public static final String TAG = "FileUtil";

    public static boolean isSDCardEnabled() {
        return (getSDCardState().equals(Environment.MEDIA_MOUNTED));
    }

    public static String getSDCardState() {
        return Environment.getExternalStorageState();
    }

    public static File getSDCardDirectory() {
        return Environment.getExternalStorageDirectory();
    }
}
