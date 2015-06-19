package com.mx.wifichat.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mx.wifichat.R;
import com.mx.wifichat.adapter.WiFiAdapter;
import com.mx.wifichat.listener.WiFiListOnItemClickListener;
import com.mx.wifichat.receiver.WifiBroadcastReceiver;
import com.mx.wifichat.util.LogUtil;
import com.mx.wifichat.util.WifiUtil;

import java.util.List;

public class WifiConfigActivity extends Activity {
    public static final String TAG = "WifiConfigActivity";

    private TextView mTxvWifiState;
    private ListView mListViewWiFis;

    private WiFiAdapter mWiFisAdapter;
    private WiFiListOnItemClickListener mWifiListener;

    private Handler mHandler;

    private WifiBroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wificonfig);
        setTitle("配置Wi-Fi");

        mHandler = new MyHandler();
        mBroadcastReceiver = new WifiBroadcastReceiver(mHandler);

        mWiFisAdapter = new WiFiAdapter(this);
        mWifiListener = new WiFiListOnItemClickListener(this);
        initViews();
        openWifi();
    }

    private void openWifi() {
        if (!WifiUtil.isWifiEnabled()) {
            if (WifiUtil.isWifiApEnabled()) {
                WifiUtil.stopAp();
            }
            WifiUtil.openWifi();
        }
    }

    private void initViews() {
        mTxvWifiState = (TextView) findViewById(R.id.txv_wifi_state);

        mListViewWiFis = (ListView) findViewById(R.id.list_wifis);
        mListViewWiFis.setAdapter(mWiFisAdapter);
        mListViewWiFis.setOnItemClickListener(mWifiListener);

        findViewById(R.id.btn_next_step).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WifiConfigActivity.this, MainActivity.class));
            }
        });

        findViewById(R.id.btn_create_ap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WifiConfigActivity.this, "Create AP...", Toast.LENGTH_SHORT).show();
                LogUtil.d(TAG, "创建结果：" + WifiUtil.startAp("WiFi_", "00000000"));
            }
        });

        findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiUtil.clearWifiConfiguration();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_wificonfig, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                break;
            case R.id.action_refresh:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        filter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
        filter.addAction("android.net.wifi.SCAN_RESULTS");
        filter.addAction("android.net.wifi.supplicant.CONNECTION_CHANGE");
        registerReceiver(mBroadcastReceiver, filter);
        WifiUtil.startScan();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WifiBroadcastReceiver.WHAT_WIFI_ENABLED:
                    if (WifiUtil.isWifiConnected())
                        mTxvWifiState.setText("已连接" + WifiUtil.getWifiInfo().getSSID());
                    else
                        mTxvWifiState.setText("Wifi已开启");
                    break;
                case WifiBroadcastReceiver.WHAT_WIFI_DISABLED:
                    mTxvWifiState.setText("Wifi已关闭");
                    break;
                case WifiBroadcastReceiver.WHAT_WIFI_CONNECTED:
                    mTxvWifiState.setText("已连接" + msg.obj);
                    break;
                case WifiBroadcastReceiver.WHAT_WIFI_DISCONNECTED:
                    if (WifiUtil.isWifiEnabled())
                        mTxvWifiState.setText("Wifi已开启");
                    else
                        mTxvWifiState.setText("Wifi已断开");
                    break;
                case WifiBroadcastReceiver.WHAT_SCAN_COMPLETED:
                    List<ScanResult> scanResults = (List<ScanResult>) msg.obj;
                    mWiFisAdapter.updateScanResult(scanResults);
                    mWifiListener.updateScanResult(scanResults);
                    break;
                case WifiBroadcastReceiver.WHAT_AP_ENABLED:
                    break;
                case WifiBroadcastReceiver.WHAT_AP_DISABLED:
                    break;
                default:
                    break;
            }
        }
    }
}
