package com.decard.mqttlibs.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.decard.mqttlibs.emqtt.MQTTApplication;

/**
 * 监听网络改变的广播
 *
 * @author ZJ
 * created at 2020/10/22 11:35
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    private static final String TAG = "---NetworkStateReceiver";
    private NetworkChangeListener mListener;

    public NetworkStateReceiver(NetworkChangeListener listener) {
        //初始化网络连接状态
        mListener = listener;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            Log.e(TAG, "onReceive: 异常");
            return;
        }
        if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            Log.d(TAG, "onReceive: 网络发生变化");
            // 获取管理网络连接的系统服务类的实例
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    MQTTApplication.Companion.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            // 判断网络是否可用
            if (networkInfo != null && networkInfo.isAvailable()) {
                Log.d(TAG, "onReceive: 网络可用");
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    Log.d(TAG, "onReceive: WIFI 可用");
                }
                mListener.connect(-1);
            } else {
                mListener.disConnect();
                Log.d(TAG, "onReceive: 网络不可用");
            }
        }
    }
}
