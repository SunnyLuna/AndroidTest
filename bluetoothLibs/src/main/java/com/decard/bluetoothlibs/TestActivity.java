package com.decard.bluetoothlibs;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.List;
import java.util.UUID;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "---TestActivity";
    private EditText et_device, et_receive, et_send;
    private Button btn_connect, btn_send;

    private static BluetoothAdapter mBluetoothAdapter;
    private boolean isScanning;
    private BluetoothDevice mBluetoothDevice;
    public static BluetoothGatt mBluetoothGatt;
    public static final UUID UUIDWRITE = UUID.fromString("0003cdd2-0000-1000-8000-00805f9b0131");
    public static final UUID UUIDCHARA = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        addPermission();
    }

    private void init() {
        Log.d(TAG, "init: ");
        btn_connect = (Button) findViewById(R.id.btn_connect);
        et_device = (EditText) findViewById(R.id.et_device);
        et_receive = (EditText) findViewById(R.id.et_receive);
        et_send = (EditText) findViewById(R.id.et_send);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取对应服务下写的的特征值
                BluetoothGattCharacteristic gattCharacteristic = gattServices.get(2).getCharacteristic(UUIDWRITE);
                if (gattCharacteristic == null) return;
                if (et_send.getText() != null) {
                    //4.将byte数据设置到特征Characteristic中去

                    gattCharacteristic.setValue(et_send.getText().toString().getBytes());

                    //将设置好的特征发送出去
                    mBluetoothGatt.writeCharacteristic(gattCharacteristic);
                    mBluetoothGatt.readCharacteristic(gattCharacteristic);
                }

            }
        });
        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isScanning) {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
                if (null == mBluetoothDevice || !mBluetoothAdapter.isEnabled()) { //设备为空或者蓝牙未开启，返回
                    return;
                }

                mBluetoothGatt = mBluetoothDevice.connectGatt(TestActivity.this, false, mGattCallback);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mBluetoothGatt.discoverServices();

            }
        });
    }


    private void addPermission() {
        //如果 API level 是大于等于 23(Android 6.0) 时判断是否具有权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //判断是否需要向用户解释为什么需要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    Toast.makeText(this, "自Android 6.0开始需要打开位置权限才可以搜索到Ble设备", Toast.LENGTH_SHORT).show();
                }
                //请求权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_CODE_ACCESS_COARSE_LOCATION);
            } else {
                //检查蓝牙是否可用 一般情况下6.0以上均支持ble开发
                checkBleSupportAndInitialize();
                init();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_ACCESS_COARSE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //用户允许改权限，0表示允许，-1表示拒绝 PERMISSION_GRANTED = 0， PERMISSION_DENIED = -1
                //这里进行授权被允许的处理
                checkBleSupportAndInitialize();
                init();
            } else {
                Toast.makeText(this, "没有获取到对应的权限", Toast.LENGTH_LONG).show();
                //这里进行权限被拒绝的处理
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    /**
     * 检查蓝牙是否可用
     */
    private void checkBleSupportAndInitialize() {
        //  检查设备是否支持ble
        if (!getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "不支持蓝牙ble",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // 初始化蓝牙适配
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null) {

            Toast.makeText(this,
                    "没有蓝牙权限", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        //打开蓝牙
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (null != mBluetoothAdapter)
            scanDevice();
    }

    Handler mHandler = new Handler();

    /**
     * 蓝牙扫描
     */
    public void scanDevice() {
        if (isScanning) {
            return;
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (!isScanning) {
                    return;
                }
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                isScanning = false;
            }
        }, 10000);
        mBluetoothAdapter.startLeScan(mLeScanCallback);
        isScanning = true;
    }

    //-------------------扫描设备

    BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            mBluetoothDevice = mBluetoothAdapter
                    .getRemoteDevice(device.getAddress());
            // TODO Auto-generated method stub
            String deviceName = device.getName();
            String deviceAddress = device.getAddress();
            if (deviceName != null && deviceName.equals("USR-BLE100")) {
                et_device.setText("名称:" + deviceName + "地址:" + deviceAddress);
                if (isScanning) {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
                if (null == mBluetoothDevice || !mBluetoothAdapter.isEnabled()) { //设备为空或者蓝牙未开启，返回
                    return;
                }

                mBluetoothGatt = mBluetoothDevice.connectGatt(TestActivity.this, false, mGattCallback);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mBluetoothGatt.discoverServices();
            } else {
                et_device.setText("没有扫描到设备");
            }

        }
    };
    Handler handler = new Handler();
    int statu;
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (statu == 0) {
                Toast.makeText(TestActivity.this, "不确定因素导致连接失败。不确定因素可能为信号太弱等", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(TestActivity.this, "由于协议栈原因导致连接建立失败。所以清除掉连接后重新建立连接。", Toast.LENGTH_SHORT).show();
            }

            mBluetoothGatt.close();

            mBluetoothGatt = mBluetoothDevice.connectGatt(TestActivity.this, false, mGattCallback);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mBluetoothGatt.discoverServices();
        }
    };
    //-------------连接回调，结果处理
    List<BluetoothGattService> gattServices;
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, final int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        int sdkInt = Build.VERSION.SDK_INT;
                        if (sdkInt >= 21) {
                            //设置最大发包、收包的长度为512个字节
                            if (requestMtu(512)) {
                                Toast.makeText(TestActivity.this, "最大传输字节为512", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(TestActivity.this, "最大传输字节为20", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TestActivity.this, "最大传输字节为20", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                statu = status;
                handler.postDelayed(mRunnable, 10000);

            }
        }


        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            Toast.makeText(TestActivity.this, "进来服务了", Toast.LENGTH_LONG).show();

            gattServices = gatt.getServices();
            //循环遍历所有的服务和特征值   找到具体的特征值进行具体的读写操作
//            for (BluetoothGattService service : gattServices) {
//                Log.d(TAG, "BluetoothGattService: " + service.getUuid());
//                for (BluetoothGattCharacteristic characteristic : service.getCharacteristics()) {
//                    Log.d(TAG, "BluetoothGattCharacteristic: " + characteristic.getUuid());
//                    for (BluetoothGattDescriptor descriptor : characteristic.getDescriptors()) {
//                        Log.d(TAG, "BluetoothGattDescriptor: " + descriptor.getUuid());
//                    }
//                }
//            }
            //获取对应服务下的notify的特征值   我这里的读写对应的是第二个服务   此处需要换成自己的
            BluetoothGattCharacteristic characteristic = gattServices.get(2).getCharacteristic(UUID.fromString("0003cdd1-0000-1000-8000-00805f9b0131"));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            prepareBroadcastDataNotify(characteristic);
        }

        //写入数据的回调
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        //读取数据的回调
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        //接收notify数据
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    byte[] bytes = characteristic.getValue();
                    et_receive.setText(byteToASCII(bytes));
                }
            });

        }
    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean requestMtu(int mtu) {
        if (mBluetoothGatt != null) {
            return mBluetoothGatt.requestMtu(mtu);
        }
        return false;
    }

    void prepareBroadcastDataNotify(
            BluetoothGattCharacteristic characteristic) {
        final int charaProp = characteristic.getProperties();
        if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
            setCharacteristicNotification(characteristic, true);
        }
    }


    private void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {

        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            return;
        }
        if (characteristic.getDescriptor(TestActivity.UUIDCHARA) != null) {
            if (enabled == true) {
                BluetoothGattDescriptor descriptor = characteristic
                        .getDescriptor(TestActivity.UUIDCHARA);
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                mBluetoothGatt.writeDescriptor(descriptor);
            } else {
                BluetoothGattDescriptor descriptor = characteristic
                        .getDescriptor(TestActivity.UUIDCHARA);
                descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                mBluetoothGatt.writeDescriptor(descriptor);
            }
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
    }

    public static String byteToASCII(byte[] array) {

        StringBuffer sb = new StringBuffer();
        for (byte byteChar : array) {
            if (byteChar >= 32 && byteChar < 127) {
                sb.append(String.format("%c", byteChar));
            } else {
                sb.append(String.format("%d ", byteChar & 0xFF));
            }
        }
        return sb.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluetoothGatt.disconnect();
        mBluetoothAdapter.disable();
    }
}
