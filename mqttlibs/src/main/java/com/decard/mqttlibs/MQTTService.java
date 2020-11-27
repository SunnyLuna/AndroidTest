package com.decard.mqttlibs;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import io.reactivex.disposables.Disposable;

public class MQTTService extends Service {

    private static final String TAG = "---MQTTService";

    private static MqttAndroidClient mMqttClient;
    private MqttConnectOptions mqttConnectOptions;
    private String host = "tcp://192.168.1.138";
    private static String myTopic = "sunnyLuna";  //要订阅的主题
    private static String topic = "TEST";  //要订阅的主题
    private String clientId = "12345";      //客户端标识
    private String userName = "admin";
    private String passWord = "password";

    private MessageCallback messageCallback;
    Disposable subscribe;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        init();
    }

    private void init() {
        // 服务器地址（协议+地址+端口号）
        mMqttClient = new MqttAndroidClient(this, host, clientId);
        // 设置MQTT监听并且接受消息
        mMqttClient.setCallback(mqttCallback);

        mqttConnectOptions = new MqttConnectOptions();
        // 清除缓存
        mqttConnectOptions.setCleanSession(true);
        // 设置超时时间，单位：秒
        mqttConnectOptions.setConnectionTimeout(10);
        // 心跳包发送间隔，单位：秒
        mqttConnectOptions.setKeepAliveInterval(20);
//        mqttConnectOptions.setAutomaticReconnect(true);
        // 用户名
        mqttConnectOptions.setUserName(userName);
        // 密码
        mqttConnectOptions.setPassword(passWord.toCharArray());     //将字符串转换为字符串数组
        // last will message
//        boolean doConnect = true;
//        String message = "{\"terminal_uid\":\"" + clientId + "\"}";
//        Log.e(TAG, "message是:" + message);
//        // 最后的遗嘱
//        // MQTT本身就是为信号不稳定的网络设计的，所以难免一些客户端会无故的和Broker断开连接。
//        //当客户端连接到Broker时，可以指定LWT，Broker会定期检测客户端是否有异常。
//        //当客户端异常掉线时，Broker就往连接时指定的topic里推送当时指定的LWT消息。
//        try {
//            mqttConnectOptions.setWill(myTopic, message.getBytes(), 0, false);
//        } catch (Exception e) {
//            Log.d(TAG, "Exception in setWill: " + e);
//            doConnect = false;
//            iMqttActionListener.onFailure(null, e);
//        }
//        if (doConnect) {
            doClientConnection();
//        }
    }


    /**
     * 监听MQTT的状态，并接收订阅的消息
     */
    private MqttCallbackExtended mqttCallback = new MqttCallbackExtended() {

        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            Log.d(TAG, "connectComplete: 是否重连" + reconnect);
            Log.d(TAG, "connectComplete: 是否连接" + mMqttClient.isConnected());

            try {
                if (reconnect) {
//                    String[] topics = new String[]{myTopic, topic};
//                    int[] qos = new int[]{1, 1};
//                    mMqttClient.subscribe(topics, qos);
                    mMqttClient.subscribe(myTopic, 1);
                }
            } catch (MqttException e) {
                Log.d(TAG, "connectComplete: " + e.getMessage());
                e.printStackTrace();
            }
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            String str1 = new String(message.getPayload());
            Log.d(TAG, "messageArrived: 主题：" + topic + "  内容：" + str1);
            if (messageCallback != null) {
                messageCallback.setMessage(str1);
            }
            String str2 = topic + ";qos:" + message.getQos() + ";retained:" + message.isRetained();
            Log.d(TAG, "messageArrived:" + str1);
            Log.d(TAG, str2);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {
            Log.d(TAG, "deliveryComplete: ");
        }

        @Override
        public void connectionLost(Throwable arg0) {
            Log.d(TAG, "connectionLost: 连接断开");
            // 失去连接，重连
            if (arg0 != null) {
                Log.d(TAG, "connectionLost: " + arg0.getMessage());
                doClientConnection();
            }
        }
    };

    /**
     * 连接MQTT服务器
     */
    private void doClientConnection() {
        Log.d(TAG, "doClientConnection: mq服务是否连接" + mMqttClient.isConnected());
        if (!mMqttClient.isConnected()) {
            try {
                mMqttClient.connect(mqttConnectOptions, null, iMqttActionListener);
            } catch (MqttException e) {
                Log.d(TAG, "doClientConnection: " + e.getMessage());
                e.printStackTrace();
            }
        }

//        subscribe = Observable.interval(0, 5, TimeUnit.SECONDS).subscribe(new Consumer<Long>() {
//            @Override
//            public void accept(Long aLong) throws Exception {
//                if (!mMqttClient.isConnected() && isConnectIsNormal()) {
//                    try {
//                        mMqttClient.connect(mqttConnectOptions, null, iMqttActionListener);
//                    } catch (MqttException e) {
//                        Log.d(TAG, "doClientConnection: " + e.getMessage());
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
    }

    /**
     * 连接MQTT的回调
     */
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {

        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.d(TAG, "连接成功 ");
            try {
                // 订阅myTopic话题
//                String[] topics = new String[]{myTopic, topic};
//                int[] qos = new int[]{1, 1};
//                mMqttClient.subscribe(topics, qos);
                mMqttClient.subscribe(myTopic, 1);
            } catch (MqttException e) {
                Log.d(TAG, "onSuccess: " + e.getMessage());
                e.printStackTrace();
            }
            if (subscribe != null) {
                subscribe.dispose();
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            Log.d(TAG, "onFailure: 连接失败：" + arg1.getMessage());
            arg1.printStackTrace();
            // 连接失败，重连
            doClientConnection();

        }
    };


    /**
     * 发布推送消息
     *
     * @param msg 发布的内容
     */
    public static void publish(String msg) {
        Log.d(TAG, "publish: " + msg.getBytes().length / 1024);
        try {
            if (mMqttClient != null) {
                //retained  发布保留标识，表示服务器要保留这次推送的信息，如果有新的订阅者出现，
                // 就把这消息推送给它，如果设有那么推送至当前订阅者后释放
                mMqttClient.publish(myTopic, msg.getBytes(), 1, false);
//                mMqttClient.publish(topic, msg.getBytes(), 1, false);
            }
        } catch (MqttException e) {
            e.printStackTrace();
            Log.d(TAG, "publish: " + e.getMessage());
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        return new CustomBinder();
    }

    public void setIGetMessageCallBack(MessageCallback messageCallBack) {
        this.messageCallback = messageCallBack;
    }

    public class CustomBinder extends Binder {
        public MQTTService getService() {
            return MQTTService.this;
        }
    }

    /**
     * 创建通知栏提示
     *
     * @param message 通知的内容
     */
    public void createNotification(String message) {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(this, MQTTService.class), PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);//3、创建一个通知，属性太多，使用构造器模式

        Notification notification = builder
                .setTicker("sunnyLuna")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("服务推送消息")
                .setContentText(message)
                .setContentInfo("")
                .setContentIntent(pendingIntent)//点击后才触发的意图，“挂起的”意图
                .setAutoCancel(true)        //设置点击之后notification消失
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        startForeground(0, notification);
        notificationManager.notify(0, notification);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            Log.d(TAG, "onDestroy: ");
            mMqttClient.unsubscribe(myTopic);
            mMqttClient.unsubscribe(topic);
            mMqttClient.clearAbortBroadcast();
            mMqttClient.unregisterResources();
            mMqttClient.close();
            mMqttClient.disconnect();
        } catch (MqttException e) {
            Log.d(TAG, "onDestroy: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
