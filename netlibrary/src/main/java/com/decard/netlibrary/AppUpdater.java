package com.decard.netlibrary;

import com.decard.netlibrary.net.INetManager;
import com.decard.netlibrary.net.RetrofitManager;

public class AppUpdater {

    private static AppUpdater appUpdater = new AppUpdater();

    //网络请求，下载的能力
    //接口隔离具体的实现

    private INetManager mNetManager = new RetrofitManager();

    public INetManager getManager() {
        return mNetManager;
    }

    public static AppUpdater getInstance() {
        return appUpdater;
    }
}
