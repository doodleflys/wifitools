package com.mx.wifichat.adapter;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mx.wifichat.R;
import com.mx.wifichat.util.WifiUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MX on 2015/6/15.
 */
public class WiFiAdapter extends BaseAdapter {
    private boolean isWifiConnected;
    private LayoutInflater mLayoutInflater;
    private List<ScanResult> mScanResults = new ArrayList<ScanResult>();
    private Context mContext;

    public WiFiAdapter(Context context) {
        super();
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void updateScanResult(List<ScanResult> scanResults) {
        mScanResults = scanResults;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mScanResults == null)
            return 0;
        else
            return mScanResults.size();
    }

    @Override
    public Object getItem(int position) {
        if (mScanResults == null)
            return null;
        else
            return mScanResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.listitem_wifi, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.listitem_img_wifi_signal_intensity);
            holder.ssid = (TextView) convertView.findViewById(R.id.listitem_txv_wifi_ssid);
            holder.desc = (TextView) convertView.findViewById(R.id.listitem_txv_wifi_desc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ScanResult wifi = (ScanResult) getItem(position);
        holder.icon.setImageResource(getIconImgId(wifi));
        holder.ssid.setText(wifi.SSID);
        holder.desc.setText(getDesc(wifi));
        return convertView;
    }

    private int getIconImgId(ScanResult ap) {
        int imgId;
        if (isWifiConnected) {
            imgId = R.drawable.ic_wifi_connected;
        } else {
            int rssi = Math.abs(ap.level);
            if (rssi > 100) {
                imgId = R.drawable.ic_small_wifi_rssi_0;
            } else if (rssi > 80) {
                imgId = R.drawable.ic_small_wifi_rssi_1;
            } else if (rssi > 70) {
                imgId = R.drawable.ic_small_wifi_rssi_2;
            } else if (rssi > 60) {
                imgId = R.drawable.ic_small_wifi_rssi_3;
            } else {
                imgId = R.drawable.ic_small_wifi_rssi_4;
            }
        }
        return imgId;
    }

    private String getDesc(ScanResult ap) {
        String desc = "";
        if (ap.SSID.startsWith("Chat_")) {
            desc = "专用网络，可以直接连接";
        } else {
            WifiUtil.WifiCipherType type = WifiUtil.getWifiCipherType(ap);
            if (type == WifiUtil.WifiCipherType.WIFI_CIPHER_WPA || type == WifiUtil.WifiCipherType.WIFI_CIPHER_WEP) {
                desc = "受到密码保护";
            } else {
                desc = "未受保护的网络";
            }
        }

        // 是否连接此热点
        if (isWifiConnected) {
            desc = "已连接";
        }
        return desc;
    }

    private int getRssiImgId(ScanResult ap) {
        int imgId;
        if (isWifiConnected) {
            imgId = R.drawable.ic_wifi_connected;
        } else {
            int rssi = Math.abs(ap.level);
            if (rssi > 100) {
                imgId = R.drawable.ic_small_wifi_rssi_0;
            } else if (rssi > 80) {
                imgId = R.drawable.ic_small_wifi_rssi_1;
            } else if (rssi > 70) {
                imgId = R.drawable.ic_small_wifi_rssi_2;
            } else if (rssi > 60) {
                imgId = R.drawable.ic_small_wifi_rssi_3;
            } else {
                imgId = R.drawable.ic_small_wifi_rssi_4;
            }
        }
        return imgId;
    }

    static class ViewHolder {
        ImageView icon;
        TextView ssid;
        TextView desc;
    }
}
