package com.decard.serialportlibs.serialport;

import android.os.HandlerThread;
import android.serialport.SerialPort;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/3/28 0028.
 */
public class SerialPortManager {
    private static final String TAG = "---SerialPortManager";
    private SerialReadThread mReadThread;
    private OutputStream mOutputStream;
    private HandlerThread mWriteThread;
    private Scheduler mSendScheduler;

    private static class InstanceHolder {

        static SerialPortManager sManager = new SerialPortManager();
    }

    public static SerialPortManager instance() {
        return InstanceHolder.sManager;
    }

    private SerialPort mSerialPort;

    /**
     * 打开串口
     *
     * @return
     */
    public SerialPort open() {
        return open("/dev/ttyHSL0", "9600");
    }

    public SerialPort openZ90N() {
        return open("/dev/ttyHSL1", "115200");
    }

    public SerialPort openF11() {
        return open("/dev/ttyS3", "115200");
    }

    public SerialPort openFile(String devicePath, String baud_rate) {
        return open(devicePath, baud_rate);
    }

    /**
     * 打开串口
     *
     * @param devicePath
     * @param baudrateString
     * @return
     */
    private SerialPort open(String devicePath, String baudrateString) {
        if (mSerialPort != null) {
            close();
        }
        try {
            File device = new File(devicePath);
            int baurate = Integer.parseInt(baudrateString);
            mSerialPort = new SerialPort(device, baurate, 0);

            mReadThread = new SerialReadThread(mSerialPort.getInputStream());
            mReadThread.start();

            mOutputStream = mSerialPort.getOutputStream();

            mWriteThread = new HandlerThread("write-thread");
            mWriteThread.start();
            mSendScheduler = AndroidSchedulers.from(mWriteThread.getLooper());
            Log.d(TAG, "open: 打开串口成功");
            return mSerialPort;
        } catch (Throwable tr) {
            Log.d(TAG, "open: 打开串口失败" + tr.getMessage());
            close();
            return null;
        }
    }

    /**
     * 关闭串口
     */
    public void close() {
        if (mReadThread != null) {
            mReadThread.close();
        }
        if (mOutputStream != null) {
            try {
                mOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (mWriteThread != null) {
            mWriteThread.quit();
        }

        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }

    /**
     * 发送数据
     *
     * @param datas
     * @return
     */
    private void sendData(byte[] datas) throws Exception {
        mOutputStream.write(datas);
    }

    /**
     * (rx包裹)发送数据
     *
     * @param datas
     * @return
     */
    private Observable<Object> rxSendData(final byte[] datas) {

        return Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                try {
                    sendData(datas);
                    emitter.onNext(new Object());
                } catch (Exception e) {
//                    logger.debug("subscribe: " + "发送：" + HexDump.bytes2HexStr(datas) + " 失败" + e);
                    if (!emitter.isDisposed()) {
                        emitter.onError(e);
                        return;
                    }
                }
                emitter.onComplete();
            }
        });
    }


    /**
     * 发送 命令
     *
     * @param command
     */
    public void sendCommand(final String command) {

//        byte[] bytes = null;
//        try {
//            bytes = command.getBytes("GBK");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        rxSendData(command.getBytes()).subscribeOn(mSendScheduler).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Object o) {
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: 发送失败" + e);
            }

            @Override
            public void onComplete() {
            }
        });
    }
}
