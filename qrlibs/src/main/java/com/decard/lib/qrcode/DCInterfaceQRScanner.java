package com.decard.lib.qrcode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;

import com.decard.dcdecodes.utils.IDcDecodeListener;
import com.example.commonlibs.utils.RxBus;
import com.tool.Scan;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.USB_SERVICE;

public class DCInterfaceQRScanner implements QRScanner {


    private static final String TAG = "---DCInterfaceQRScanner";
    private UsbManager myUsbManager;
    private ScanDevice mScanDevice;
    private Scan myScan;
    private static final int gPID = 0x8004;
    private static final int gVID = 0xAF99;
    private IDcDecodeListener mListener;
    private volatile Disposable subscribe;

    private static class Holder {
        private static DCInterfaceQRScanner instance = new DCInterfaceQRScanner();
    }

    public static DCInterfaceQRScanner getInstance() {
        return DCInterfaceQRScanner.Holder.instance;
    }

    @Override
    public void initData(String devicePath, int baudRate, IDcDecodeListener listener) {

    }

    @Override
    public void initData(Context context, IDcDecodeListener listener) {
        this.mListener = listener;
        if (myScan == null && myUsbManager == null) {
            myScan = new Scan(context, myHandler);
            myUsbManager = (UsbManager) context.getSystemService(USB_SERVICE);
            myScan.StartUsbChk(myUsbManager, gPID, gVID);
        }
    }

    @Override
    public void initData(Context context, SurfaceView surfaceView, int cameraId, IDcDecodeListener listener) {

    }


    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.d(TAG, "handleMessage: " + msg);
            int deviceId;
            deviceId = msg.arg1;
            switch (msg.what) {
                case Scan.HANDLER_SCAN_STATUS:
                    mScanDevice = new ScanDevice(deviceId);
                    mScanDevice.setStatus(ScanDevice.DEVICE_CONNECT);
                    mListener.onActiveResult("激活成功");
                    break;
                case Scan.HANDLER_SCAN_ONE_DEC:
                    Log.d(TAG, "HANDLER_SCAN_ONE_DEC: " + (String) msg.obj);
                    mListener.onDecodeResult(((String) msg.obj).trim().getBytes());
                case Scan.HANDLER_SCAN_TWO_DEC:
                    Log.d(TAG, "HANDLER_SCAN_TWO_DEC: " + (String) msg.obj);
                    mListener.onDecodeResult(((String) msg.obj).trim().getBytes());
                    break;
                case Scan.HANDLER_SCAN_TEXT:
                    break;
                case Scan.HANDLER_SCAN_CLEAR:

                    break;
            }
        }
    };


    @Override
    public boolean startScanner() {
        if (mScanDevice == null) {
            Log.d(TAG, "startScanner: 开启扫码失败");
            return false;
        }
        Log.d(TAG, "startScanner: 开启扫码" + Thread.currentThread().getName());
        subscribe = Observable.interval(2, 1, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                try {
                    myScan.StartDecode(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return true;
    }

    @Override
    public void stopScanner() {
        Log.d(TAG, "stopScanner: 停止扫码");
        stop(false);
    }

    private void stop(boolean isExit) {
        RxBus.Companion.getInstance().rxBusUnbind(subscribe);

        Disposable subscribe = Observable.timer(50, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                try {
                    myScan.StopDecode(0);
                    if (isExit) {
                        myScan.exit();
                        myScan.StopUsbChk();
                        myScan = null;
                        myUsbManager = null;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "stopScanner: " + e.getMessage());
                }
            }
        });

    }

    @Override
    public void release() {
        stop(true);
    }
}
