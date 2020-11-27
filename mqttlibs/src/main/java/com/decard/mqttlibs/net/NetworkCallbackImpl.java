package com.decard.mqttlibs.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.util.Log;

import com.decard.mqttlibs.MQTTApplication;

/**
 * 监听网络
 *
 * @author ZJ
 * created at 2020/10/20 11:48
 */
public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {

    private static final String TAG = "---NetworkCallbackImpl";
    private NetworkChangeListener netWorkChangeListener;

    public NetworkCallbackImpl(NetworkChangeListener netWorkChangeListener) {
        this.netWorkChangeListener = netWorkChangeListener;
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        NetworkRequest build = builder.build();
        ConnectivityManager connectivityManager = (ConnectivityManager) MQTTApplication.Companion.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.registerNetworkCallback(build, this);
    }


    /**
     * 网络可用的时候调用
     *
     * @param network
     */
    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        Log.d(TAG, "onAvailable: 网络已连接");
        netWorkChangeListener.connect(-1);
    }


    /**
     * 网络断开时调用
     *
     * @param network
     */
    @Override
    public void onLost(Network network) {
        super.onLost(network);
        netWorkChangeListener.disConnect();
        Log.d(TAG, "onLost: 网络已断开");
    }

    /**
     * 网络正在减弱，链接会丢失数据，即将断开网络时调用
     *
     * @param network
     * @param maxMsToLive
     */
    @Override
    public void onLosing(Network network, int maxMsToLive) {
        super.onLosing(network, maxMsToLive);
    }

    /**
     * 网络功能发生改变时调用
     *
     * @param network
     * @param networkCapabilities
     */
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
