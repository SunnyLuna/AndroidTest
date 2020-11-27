package com.decard.mqttlibs.net;

import android.annotation.SuppressLint;
import android.app.usage.NetworkStats;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.os.Build;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;

import static android.content.Context.NETWORK_STATS_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;

/**
 * @author hizha
 * @brief 流量统计工具类
 * @email zw@decard.com
 * @date 2020/6/18
 * @attention  需要6.0以上
 *      需要有以下权限
 *      <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS" tools:ignore="ProtectedPermissions"/>
 *      <uses-permission android:name="android.permission.CALL_PHONE" />
 *      <uses-permission android:name="android.permission.MODIFY_PHONE_STATE" />
 */
public class TrafficStatisticsUtils {

    private static TrafficStatisticsUtils instance;
    public static int TYPE_WIFI = 1; //wifi
    public static int TYPE_MOBILE = 0; //移动网络
    public static int TYPE_ETHERNET = 9; //以太网
    private static NetworkStatsManager networkStatsManager;
    private static TelephonyManager telephonyManager;



    private static TrafficStatisticsUtils getInstance() {
        if (instance == null) {
            instance = new TrafficStatisticsUtils();
        }
        return instance;
    }

    /**
     *
     * @param context 上下文
     * @param netType 网络类型   TrafficStatisticsUtils.TYPE_WIFI   TrafficStatisticsUtils.TYPE_MOBILE    TrafficStatisticsUtils.TYPE_ETHERNET
     * @return 查询netType网络指所有流量
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static long getTrafficStatistics(Context context, int netType) {
        return getTrafficStatistics(context,netType,0,System.currentTimeMillis());
    }

    /**
     * @param context 上下文
     * @param netType 网络类型   TrafficStatisticsUtils.TYPE_WIFI   TrafficStatisticsUtils.TYPE_MOBILE    TrafficStatisticsUtils.TYPE_ETHERNET
     * @param startTime  开始时间
     * @param endTime   结束时间
     * @return  查询netType网络指定时间段内所有流量
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("MissingPermission")
    public static long getTrafficStatistics(Context context, int netType, long startTime, long endTime) {
        if (networkStatsManager == null) {
            networkStatsManager = (NetworkStatsManager) context.getSystemService(NETWORK_STATS_SERVICE);
        }

        // 获取subscriberId
        if (telephonyManager == null) {
            telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        }

        //查询设备总的流量统计信息
        NetworkStats.Bucket bucket = null;//获取到目前为止设备的TYPE_MOBILE流量统计

        try {

            String subId = "";
            if (netType == TYPE_MOBILE) { //只有是移动网络的时候才获取
                subId = telephonyManager.getSubscriberId();
            }

            bucket = networkStatsManager.querySummaryForDevice(netType, subId, startTime, endTime);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        long total = 0;
        if (bucket != null) {
            total = bucket.getRxBytes() + bucket.getTxBytes();
            Log.i("Info", "bucket_TYPE_MOBILE: " + (bucket.getRxBytes() + bucket.getTxBytes()));
        }
        return total;
    }
}
