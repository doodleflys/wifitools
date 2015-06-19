package com.mx.wifichat.listener;

import android.app.Dialog;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.View;
import android.widget.AdapterView;

import com.mx.wifichat.MyApplication;
import com.mx.wifichat.util.LogUtil;
import com.mx.wifichat.util.WifiUtil;
import com.mx.wifichat.view.WifiConfigDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr on 2015/6/15.
 */
public class WiFiListOnItemClickListener implements AdapterView.OnItemClickListener {
    public static final String TAG = "WiFiListOnItemClickListener";

    private Context mContext;
    private List<ScanResult> mScanResults = new ArrayList<ScanResult>();

    public WiFiListOnItemClickListener(Context mContext) {
        this.mContext = mContext;
    }

    public void updateScanResult(List<ScanResult> scanResults) {
        mScanResults = scanResults;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogUtil.d(TAG, WifiUtil.getScanResults().get(position).toString());
        ScanResult scanResult = mScanResults.get(position);
        WifiUtil.WifiCipherType type = WifiUtil.getWifiCipherType(scanResult);
        if (WifiUtil.hasConfiged(scanResult)) {
            WifiUtil.connect(WifiUtil.getConfiguration(mScanResults.get(position)));
        } else if (type == WifiUtil.WifiCipherType.WIFI_CIPHER_NO_PASSWORD) {
            WifiUtil.connect(scanResult);
        } else {
            new WifiConfigDialog(mContext, scanResult).show();
        }
    }
}
