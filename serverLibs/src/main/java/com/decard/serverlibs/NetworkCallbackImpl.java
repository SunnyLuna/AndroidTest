package com.decard.serverlibs;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.util.Log;

public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {

    private static final String TAG = "---NetworkCallbackImpl";
    private NetWorkChangeListener netWorkChangeListener;

    public NetworkCallbackImpl(NetWorkChangeListener netWorkChangeListener) {
        this.netWorkChangeListener = netWorkChangeListener;
    }

    public interface NetWorkChangeListener {
        void connect(int i);

        void disConnect();
    }

    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        Log.d(TAG, "onAvailable: 网络已连接");
        netWorkChangeListener.connect(0);
    }

    @Override
    public void onLost(Network network) {
        super.onLost(network);
        netWorkChangeListener.disConnect();
        Log.d(TAG, "onLost: 网络已断开");
    }

    @Override
    public void onLosing(Network network, int maxMsToLive) {
        super.onLosing(network, maxMsToLive);
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.d(TAG, "onCapabilitiesChanged: 网络类型为wifi");
                netWorkChangeListener.connect(NetworkCapabilities.TRANSPORT_WIFI);
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.d(TAG, "onCapabilitiesChanged: 蜂窝网络");
                netWorkChangeListener.connect(NetworkCapabilities.TRANSPORT_CELLULAR);
            } else if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.d(TAG, "onCapabilitiesChanged: 以太网");
                netWorkChangeListener.connect(NetworkCapabilities.TRANSPORT_ETHERNET);
            } else {
                Log.d(TAG, "onCapabilitiesChanged: 其他");
            }
        }
    }


}
