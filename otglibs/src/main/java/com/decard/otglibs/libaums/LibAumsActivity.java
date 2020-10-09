package com.decard.otglibs.libaums;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.decard.otglibs.R;
import com.github.mjdev.libaums.UsbMassStorageDevice;
import com.github.mjdev.libaums.fs.FileSystem;
import com.github.mjdev.libaums.fs.UsbFile;
import com.github.mjdev.libaums.fs.UsbFileStreamFactory;
import com.github.mjdev.libaums.partition.Partition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LibAumsActivity extends AppCompatActivity {
    private static final String TAG = "---LibAumsActivity";
    private static final String ACTION_USB_PERMISSION = "com.android.usb.USB_PERMISSION";
    private UsbManager mUsbManager;
    private PendingIntent mPermissionIntent;

    private BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: " + intent);
            String action = intent.getAction();
            if (action == null)
                return;
            switch (action) {
                case ACTION_USB_PERMISSION://用户授权广播
                    synchronized (this) {
                        if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) { //允许权限申请
                            Log.d(TAG, "onReceive: U盘授权成功");
                            Observable.timer(3, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).subscribe(new Consumer<Long>() {
                                @Override
                                public void accept(Long aLong) throws Exception {
                                    test();
                                }
                            });
                        } else {
                            Log.d(TAG, "onReceive: 用户未授权，访问USB设备失败");
                        }
                    }
                    break;
                case UsbManager.ACTION_USB_DEVICE_ATTACHED://USB设备插入广播
                    UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    mUsbManager.requestPermission(usbDevice, mPermissionIntent);
                    Log.d(TAG, "onReceive: USB设备插入");
                    break;
                case UsbManager.ACTION_USB_DEVICE_DETACHED://USB设备拔出广播
                    Log.d(TAG, "onReceive: USB设备拔出");
                    break;
            }
        }
    };

    private void init() {
        //USB管理器
        mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);

        //注册广播,监听USB插入和拔出
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        intentFilter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        registerReceiver(mUsbReceiver, intentFilter);
        //注册监听自定义广播
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);

        //读写权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 111);
        }
        test();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_aums);
        init();
    }

    private void test() {
        try {
            UsbMassStorageDevice[] storageDevices = UsbMassStorageDevice.getMassStorageDevices(this);
            Log.d(TAG, "test: " + storageDevices.length);
            for (UsbMassStorageDevice storageDevice : storageDevices) { //一般手机只有一个USB设备
                // 申请USB权限
                if (!mUsbManager.hasPermission(storageDevice.getUsbDevice())) {
                    mUsbManager.requestPermission(storageDevice.getUsbDevice(), mPermissionIntent);
                    break;
                }
                // 初始化
                storageDevice.init();
                // 获取分区
                List<Partition> partitions = storageDevice.getPartitions();
                if (partitions.size() == 0) {
                    Log.d(TAG, "错误: 读取分区失败");
                    return;
                }
                // 仅使用第一分区
                FileSystem fileSystem = partitions.get(0).getFileSystem();
                Log.d(TAG, "Volume Label: " + fileSystem.getVolumeLabel());
                Log.d(TAG, "Capacity: " + fSize(fileSystem.getCapacity()));
                Log.d(TAG, "Occupied Space: " + fSize(fileSystem.getOccupiedSpace()));
                Log.d(TAG, "Free Space: " + fSize(fileSystem.getFreeSpace()));
                Log.d(TAG, "Chunk size: " + fSize(fileSystem.getChunkSize()));

                UsbFile root = fileSystem.getRootDirectory();

                UsbFile[] files = root.listFiles();
                for (UsbFile file : files) {
                    Log.d(TAG, "文件: " + file.getName() + file.getAbsolutePath());
                }
                // 新建文件
                UsbFile newFile = root.createFile("hello_" + System.currentTimeMillis() + ".txt");
                Log.d(TAG, "新建文件: " + newFile.getName());
                // 写文件
                // OutputStream os = new UsbFileOutputStream(newFile);
                OutputStream os = UsbFileStreamFactory.createBufferedOutputStream(newFile, fileSystem);
                os.write(("hi_" + System.currentTimeMillis()).getBytes());
                os.close();
                Log.d(TAG, "写文件: " + newFile.getName());

                // 读文件
                // InputStream is = new UsbFileInputStream(newFile);
                InputStream is = UsbFileStreamFactory.createBufferedInputStream(newFile, fileSystem);
                byte[] buffer = new byte[fileSystem.getChunkSize()];
                int len;
                File sdFile = new File("/sdcard/111");
                sdFile.mkdirs();
                FileOutputStream sdOut = new FileOutputStream(sdFile.getAbsolutePath() + "/" + newFile.getName());
                while ((len = is.read(buffer)) != -1) {
                    sdOut.write(buffer, 0, len);
                }
                is.close();
                sdOut.close();
                Log.d(TAG, "读文件: " + newFile.getName() + " ->复制到/sdcard/111/");
                storageDevice.close();
            }
        } catch (Exception e) {
            Log.d(TAG, "错误: " + e);
        }
    }

    public static String fSize(long sizeInByte) {
        if (sizeInByte < 1024)
            return String.format("%s", sizeInByte);
        else if (sizeInByte < 1024 * 1024)
            return String.format(Locale.CANADA, "%.2fKB", sizeInByte / 1024.);
        else if (sizeInByte < 1024 * 1024 * 1024)
            return String.format(Locale.CANADA, "%.2fMB", sizeInByte / 1024. / 1024);
        else
            return String.format(Locale.CANADA, "%.2fGB", sizeInByte / 1024. / 1024 / 1024);
    }

}
