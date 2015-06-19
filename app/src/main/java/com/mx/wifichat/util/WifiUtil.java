package com.mx.wifichat.util;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;

import com.mx.wifichat.MyApplication;

import java.lang.reflect.Method;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by MX on 2015/6/16.
 */
public class WifiUtil {

    public static final String TAG = "WifiUtil";

    // Ap状态：正在关闭
    public static final int WIFI_AP_STATE_DISABLING = 10;
    // Ap状态：已关闭
    public static final int WIFI_AP_STATE_DISABLED = 11;
    // Ap状态：正在启用
    public static final int WIFI_AP_STATE_ENABLING = 12;
    // Ap状态：已启用
    public static final int WIFI_AP_STATE_ENABLED = 13;
    // Ap状态：获取失败
    public static final int WIFI_AP_STATE_FAILED = 14;

    // 加密方式
    public static enum WifiCipherType {
        WIFI_CIPHER_WEP, WIFI_CIPHER_WPA, WIFI_CIPHER_NO_PASSWORD, WIFI_CIPHER_INVALID
    }

    // Wifi管理服务
    private static WifiManager mWifiManager = (WifiManager) MyApplication.getInstance().getSystemService(Context.WIFI_SERVICE);

    // 当前Wifi连接信息
    private static WifiInfo mWifiConnectionInfo;

    // Dhcp信息
    private static DhcpInfo mDhcpInfo;

    // Wifi锁（需要先创建，再启用）
    private static WifiLock mWifiLock;

    // 扫描结果
    private static List<ScanResult> mScanResults;

    // Wifi配置
    private static List<WifiConfiguration> mWifiConfigurations;

    /**
     * 打开Wifi
     */
    public static void openWifi() {
        LogUtil.d(TAG, "openWifi()");
        if (!mWifiManager.isWifiEnabled())
            mWifiManager.setWifiEnabled(true);
    }

    /**
     * 关闭Wifi
     */
    public static void closeWifi() {
        LogUtil.d(TAG, "closeWifi()");
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    /**
     * Wifi是否可用
     */
    public static boolean isWifiEnabled() {
        LogUtil.d(TAG, "isWifiEnabled()");
        return mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
    }

    /**
     * 是否已连接Wifi
     */
    public static boolean isWifiConnected() {
        getWifiInfo();
        return mWifiConnectionInfo.getNetworkId() != -1;
    }

    /**
     * 创建一个WifiLock
     */
    public static void createWifiLock(String tag) {
        LogUtil.d(TAG, "createWifiLock()");
        // WIFI_MODE_FULL_HIGH_PERF会保持激活状态，保持高性能与最低丢包率（即使屏幕已关闭）
        // WIFI_MODE_SCAN_ONLY Wifi会保持激活状态，不会自动扫描，但是可以明确调用startScan()方法进行扫描，并获得扫描结果
        // WIFI_MODE_FULL Wifi会保持激活状态，并定期扫描，自动连接在范围内的记住的接入点
        mWifiLock = mWifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, tag);
    }

    /**
     * 锁定WifiLock
     */
    public static void acquireWifiLock() {
        LogUtil.d(TAG, "acquireWifiLock()");
        mWifiLock.acquire();
    }

    /**
     * 解锁WifiLock
     */
    public static void releaseWifiLock() {
        LogUtil.d(TAG, "releaseWifiLock()");
        // 判断是否锁定
        if (mWifiLock.isHeld()) {
            mWifiLock.acquire();
        }
    }

    /**
     * 设置WifiLock引用计数是否可用
     */
    public static void setReferenceCounted(boolean refCounted) {
        LogUtil.d(TAG, "setReferenceCounted()");
        mWifiLock.setReferenceCounted(refCounted);
    }

    /**
     * 得到已配置的Wifi
     */
    public static List<WifiConfiguration> getConfigurations() {
        LogUtil.d(TAG, "getConfigurations()");
        mWifiConfigurations = mWifiManager.getConfiguredNetworks();
        return mWifiConfigurations;
    }

    /**
     * 开始扫描接入点
     */
    public static void startScan() {
        LogUtil.d(TAG, "startScan()");
        mWifiManager.startScan();
    }

    /**
     * 得到网络列表
     */
    public static List<ScanResult> getScanResults() {
        LogUtil.d(TAG, "getScanResults()");
        mScanResults = mWifiManager.getScanResults();
        return mScanResults;
    }

    /**
     * 获取当前连接信息
     */
    public static WifiInfo getWifiInfo() {
        LogUtil.d(TAG, "getWifiInfo()");
        mWifiConnectionInfo = mWifiManager.getConnectionInfo();
        return mWifiConnectionInfo;
    }

    /**
     * 添加一个网络配置，默认设置该网络为disabled
     */
    public static int addNetWork(WifiConfiguration config) {
        LogUtil.d(TAG, "addNetWork()");
        int netId = mWifiManager.addNetwork(config);
        mWifiManager.saveConfiguration();
        return netId;
    }

    /**
     * 启用指定netId的网络，并设置其他网络是否可用
     */
    public static boolean enableNetwork(int netId, boolean disableOthers) {
        LogUtil.d(TAG, "enableNetwork()");
        return mWifiManager.enableNetwork(netId, disableOthers);
    }

    /**
     * 断开Wifi连接
     */
    public static boolean disconnect() {
        LogUtil.d(TAG, "disconnect()");
        return mWifiManager.disconnect();
    }

    /**
     * 获取加密类型
     */
    public static WifiCipherType getWifiCipherType(ScanResult result) {
        // LogUtil.d(TAG, "getWifiCipherType()");
        WifiCipherType type = WifiCipherType.WIFI_CIPHER_INVALID;
        if (result.capabilities.contains("WEP")) {
            type = WifiCipherType.WIFI_CIPHER_WEP;
        } else if (result.capabilities.contains("WPA")) {
            type = WifiCipherType.WIFI_CIPHER_WPA;
        } else {
            type = WifiCipherType.WIFI_CIPHER_NO_PASSWORD;
        }
        return type;
    }

    /**
     * 连接已配置的Wifi
     */
    public static boolean connect(WifiConfiguration wifiConfig) {
        LogUtil.d(TAG, "connect()");
        disconnect();
        return enableNetwork(wifiConfig.networkId, true);
    }

    /**
     * 连接无密码的Wifi
     */
    public static boolean connect(ScanResult result) {
        LogUtil.d(TAG, "connect()");
        disconnect();
        return connect(result, null);
    }

    /**
     * 连接未配置的Wifi
     */
    public static boolean connect(ScanResult result, String password) {
        LogUtil.d(TAG, "connect()");
        WifiConfiguration config = createConfiguration(result.SSID, password, getWifiCipherType(result));
        if (config == null) {
            return false;
        }
        int netId = mWifiManager.addNetwork(config);
        disconnect();
        return enableNetwork(netId, true);
    }

    /**
     * 获得指定ssid和bssid的本机Wifi配置
     */
    public static WifiConfiguration getConfiguration(ScanResult result) {
        LogUtil.d(TAG, "getConfiguration()");
        getConfigurations();
        if (mWifiConfigurations != null)
            for (WifiConfiguration config : mWifiConfigurations) {
                if (config.SSID.equals("\"" + result.SSID + "\"")) {
                    // 没有密码的网络，配置中没有BSSID这一项
                    if (config.BSSID == null || config.BSSID.equals(result.BSSID))
                        return config;
                }
            }
        return null;
    }

    /**
     * 是否连接过
     */
    public static boolean hasConfiged(ScanResult result) {
        if (getConfiguration(result) == null)
            return false;
        else
            return true;
    }

    /**
     * 创建Wifi配置
     */
    private static WifiConfiguration createConfiguration(String ssid, String password, WifiCipherType type) {
        LogUtil.d(TAG, "createConfiguration()");
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + ssid + "\"";
        if (type == WifiCipherType.WIFI_CIPHER_NO_PASSWORD) {
            //config.wepKeys[0] = "";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            //config.wepTxKeyIndex = 0;
        } else if (type == WifiCipherType.WIFI_CIPHER_WEP) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        } else if (type == WifiCipherType.WIFI_CIPHER_WPA) {
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        } else {
            return null;
        }
        return config;
    }

    /**
     * 移除指定netId的网络配置
     */
    public static boolean removeNetwork(int netId) {
        LogUtil.d(TAG, "removeNetwork()");
        boolean b = mWifiManager.removeNetwork(netId);
        mWifiManager.saveConfiguration();
        return b;
    }

    /**
     * 清除所有配置
     */
    public static boolean clearWifiConfiguration() {
        LogUtil.d(TAG, "clearWifiConfiguration()");
        getConfigurations();
        if (mWifiConfigurations != null)
            for (WifiConfiguration config : mWifiConfigurations)
                mWifiManager.removeNetwork(config.networkId);
        return mWifiManager.saveConfiguration();
    }

    /**
     * 获取MAC地址
     */
    public static String getMacAddress() {
        LogUtil.d(TAG, "getMacAddress()");
        getWifiInfo();
        if (mWifiConnectionInfo != null)
            return mWifiConnectionInfo.getMacAddress();
        return null;
    }

    /**
     * 获取Gateway
     */
    public static String getGateway() {
        LogUtil.d(TAG, "getGateway()");
        getDhcpInfo();
        if (mDhcpInfo != null) {
            int gateway = mDhcpInfo.gateway;
            return (gateway & 0xFF) + "." + ((gateway >> 8) & 0xFF) + "." + ((gateway >> 16) & 0xFF) + "." + ((gateway >> 24) & 0xFF);
        }
        return null;
    }

    /**
     * 获取DhcpInfo
     */
    public static DhcpInfo getDhcpInfo() {
        LogUtil.d(TAG, "getDhcpInfo()");
        mDhcpInfo = mWifiManager.getDhcpInfo();
        return mDhcpInfo;
    }

    /**
     * 获得广播地址
     */
    public static String getBroadcastAddress() {
        LogUtil.d(TAG, "getBroadcastAddress()");
        System.setProperty("java.net.preferIPv4Stack", "true");
        try {
            for (Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces(); niEnum.hasMoreElements(); ) {
                NetworkInterface ni = niEnum.nextElement();
                if (!ni.isLoopback()) {
                    for (InterfaceAddress interfaceAddress : ni.getInterfaceAddresses()) {
                        if (interfaceAddress.getBroadcast() != null) {
                            return interfaceAddress.getBroadcast().toString().substring(1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * Wifi接入点是否可用
     */
    public static boolean isWifiApEnabled() {
        LogUtil.d(TAG, "isWifiApEnabled()");
        try {
            Method method = mWifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(mWifiManager);
        } catch (Exception e) {
            LogUtil.e(TAG, e.getMessage());
        }
        return false;
    }

    /**
     * 获取Ap状态
     */
    private static int getWifiApStateInt() {
        LogUtil.d(TAG, "getWifiApStateInt()");
        try {
            Method method = mWifiManager.getClass().getMethod("getWifiApState");
            method.setAccessible(true);
            int state = (int) method.invoke(mWifiManager);
            //int i = ((Integer) mWifiManager.getClass().getMethod("getWifiApState", new Class[0]).invoke(mWifiManager, new Object[0])).intValue();
            return state;
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
        return WIFI_AP_STATE_FAILED;
    }

    /**
     * 使用config启动Ap，如果Ap已经启动，用config更新现有配置
     */
    private static boolean setWifiApEnabled(WifiConfiguration config, boolean enabled) {
        LogUtil.d(TAG, "setWifiApEnabled()");
        try {
            Method method = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, boolean.class);
            method.setAccessible(true);
            return (boolean) method.invoke(mWifiManager, config, enabled);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
        return false;
    }

    /**
     * 获得当前Ap设置
     */
    private static WifiConfiguration getWifiApConfiguration() {
        LogUtil.d(TAG, "getWifiApConfiguration()");
        try {
            Method method = mWifiManager.getClass().getMethod("getWifiApConfiguration");
            method.setAccessible(true);
            return (WifiConfiguration) method.invoke(mWifiManager);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
        return null;
    }

    /**
     * 创建一个Ap设置
     */
    private static WifiConfiguration createApConfig(String ssid, String passwd) {
        LogUtil.d(TAG, "createApConfig()");
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = ssid;
        config.preSharedKey = passwd;
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
        config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        return config;
    }

    /**
     * 更新Ap设置
     */
    private static boolean setWifiApConfiguration(WifiConfiguration config) {
        LogUtil.d(TAG, "setWifiApConfiguration()");
        try {
            Method method = mWifiManager.getClass().getMethod("setWifiApConfiguration", WifiConfiguration.class);
            method.setAccessible(true);
            return (boolean) method.invoke(mWifiManager, config);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
        return false;
    }


    /**
     * 打开Ap
     */
    public static boolean startAp(String ssid, String passwd) {
        LogUtil.d(TAG, "startAp()");
        //先关闭Wifi才能启动Ap模式
        if (isWifiEnabled())
            closeWifi();
        WifiConfiguration config = createApConfig(ssid, passwd);
        return setWifiApEnabled(config, true);
    }

    /**
     * 关闭Ap
     */
    public static boolean stopAp() {
        LogUtil.d(TAG, "stopAp()");
        if (getWifiApStateInt() == WIFI_AP_STATE_ENABLED)
            return setWifiApEnabled(getWifiApConfiguration(), false);
        return false;
    }
}
