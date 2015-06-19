package com.mx.wifichat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;

import com.mx.wifichat.util.LogUtil;
import com.mx.wifichat.util.WifiUtil;

import java.util.List;

/**
 * Created by MX on 2015/6/16.
 */
public class WifiBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = "WifiBroadcastReceiver";

    public static final String CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final int WHAT_WIFI_CONNECTED = 9;
    public static final int WHAT_WIFI_DISCONNECTED = 10;

    public static final String SCAN_RESULTS = "android.net.wifi.SCAN_RESULTS";
    public static final int WHAT_SCAN_COMPLETED = 1;

    public static final String WIFI_STATE_CHANGED = "android.net.wifi.WIFI_STATE_CHANGED";
    public static final int WHAT_WIFI_ENABLED = 2;
    public static final int WHAT_WIFI_DISABLED = 3;

    public static final String WIFI_AP_STATE_CHANGED = "android.net.wifi.WIFI_AP_STATE_CHANGED";
    public static final int WHAT_AP_ENABLED = 6;
    public static final int WHAT_AP_DISABLED = 7;

    public static final String STATE_CHANGE = "android.net.wifi.STATE_CHANGE";
    public static final int WHAT_CHANGE = 8;

    private Handler mHandler;

    public WifiBroadcastReceiver(Handler mHandler) {
        this.mHandler = mHandler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LogUtil.d(TAG, action);
        Message msg = mHandler.obtainMessage();
        msg.what = 0;
        switch (action) {
            case SCAN_RESULTS:
                msg.what = WHAT_SCAN_COMPLETED;
                msg.obj = WifiUtil.getScanResults();
                break;
            case WIFI_STATE_CHANGED:
                if (WifiUtil.isWifiEnabled()) {
                    msg.what = WHAT_WIFI_ENABLED;
                } else {
                    msg.what = WHAT_WIFI_DISABLED;
                }
                break;
            case CONNECTIVITY_CHANGE:
                if (WifiUtil.isWifiConnected()) {
                    msg.what = WHAT_WIFI_CONNECTED;
                    msg.obj = WifiUtil.getWifiInfo().getSSID();
                } else {
                    msg.what = WHAT_WIFI_DISCONNECTED;
                }
                break;
            case WIFI_AP_STATE_CHANGED:
                if (WifiUtil.isWifiApEnabled()) {
                    msg.what = WHAT_AP_ENABLED;
                } else {
                    msg.what = WHAT_AP_DISABLED;
                }
                break;
            case STATE_CHANGE:
                LogUtil.d(TAG, intent.getExtras().toString());
                break;
        }
        if (msg.what != 0) {
            mHandler.sendMessage(msg);
        }
    }

}
