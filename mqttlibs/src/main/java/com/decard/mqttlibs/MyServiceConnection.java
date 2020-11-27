package com.decard.mqttlibs;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

class MyServiceConnection  implements ServiceConnection {

    private MQTTService mqttService;
    private MessageCallback IGetMessageCallBack;

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mqttService = ((MQTTService.CustomBinder)iBinder).getService();
        mqttService.setIGetMessageCallBack(IGetMessageCallBack);
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    public MQTTService getMqttService(){
        return mqttService;
    }

    public void setIGetMessageCallBack(MessageCallback IGetMessageCallBack){
        this.IGetMessageCallBack = IGetMessageCallBack;
    }
}

