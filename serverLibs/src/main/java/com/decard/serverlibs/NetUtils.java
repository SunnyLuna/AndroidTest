/*
 * Copyright © 2018 Yan Zhenjie.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.decard.serverlibs;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 *
 * @author ZJ
 * created at 2020/10/14 14:09
 */
public class NetUtils {
    private static final String TAG = "----NetUtils";
    /**
     * Ipv4 address check.
     */
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^(" + "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}" +
                    "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");

    /**
     * Check if valid IPV4 address.
     *
     * @param input the address string to check for validity.
     * @return True if the input parameter is a valid IPv4 address.
     */
    public static boolean isIPv4Address(String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

    /**
     * Get local Ip address.
     */
    public static InetAddress getLocalIPAddress() {
        Enumeration<NetworkInterface> enumeration = null;
        InetAddress mInetAddress = null;
        try {
            enumeration = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                NetworkInterface nif = enumeration.nextElement();
//                Log.d(TAG, "getLocalIPAddress: " + nif.getName());
                Enumeration<InetAddress> inetAddresses = nif.getInetAddresses();
                if (inetAddresses != null) {
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        if (!inetAddress.isLoopbackAddress() && isIPv4Address(inetAddress.getHostAddress())) {
                            if (nif.getName().equals("eth0")) {
                                return inetAddress;
                            }
                            mInetAddress = inetAddress;
                        }
                    }
                }
            }
        }
        return mInetAddress;
    }

    public static boolean isEth() {
        ConnectivityManager connMgr = (ConnectivityManager) MyApp.Companion.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr == null) {
            return false;
        }
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        }
        int nType = networkInfo.getType();
//        Log.d(TAG, "getNetType: " + nType);
        return nType == ConnectivityManager.TYPE_WIFI || nType == ConnectivityManager.TYPE_ETHERNET;
    }

}
