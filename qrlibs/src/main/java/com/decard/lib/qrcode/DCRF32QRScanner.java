package com.decard.lib.qrcode;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceView;

import com.decard.NDKMethod.BasicOper;
import com.decard.dcdecodes.utils.IDcDecodeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * dcrf32硬扫
 *
 * @author ZJ
 * created at 2020/7/31 16:24
 */
public class DCRF32QRScanner implements QRScanner {
    private static final String TAG = "DCRF32QRScanner";
    private static Logger logger = LoggerFactory.getLogger("DCRF32QRScanner");
    private IDcDecodeListener mListener;
    private volatile boolean flag = false;

    public DCRF32QRScanner() {
    }

    private static class Holder {
        private static DCRF32QRScanner instance = new DCRF32QRScanner();
    }

    public static DCRF32QRScanner getInstance() {
        return Holder.instance;
    }

    int dc_open = -1;

    @Override
    public void initData(String devicePath, int baudRate, IDcDecodeListener listener) {
        mListener = listener;
        dc_open = -1;
        while (dc_open < 0) {
            dc_open = BasicOper.dc_open("COM", null, devicePath, baudRate);
            logger.debug(dc_open + "");
        }
        String ret = BasicOper.dc_getver();
        logger.debug(ret);
        Log.d(TAG, "initData: " + ret);
        if (ret != null && ret.startsWith("0000")) {
            listener.onActiveResult("激活成功");
        } else {
            listener.onActiveResult("端口打开失败");
        }
    }

    @Override
    public void initData(Context context, IDcDecodeListener listener) {
        mListener = listener;
        dc_open = -1;
        while (dc_open < 0) {
            dc_open = BasicOper.dc_open("AUSB", context, "", 0);
            logger.debug(dc_open + "");
        }
        String ret = BasicOper.dc_getver();
        logger.debug(ret);
        Log.d(TAG, "initData: " + ret);
        if (ret != null && ret.startsWith("0000")) {
            listener.onActiveResult("激活成功");
        } else {
            listener.onActiveResult("端口打开失败");
        }
    }

    @Override
    public void initData(Context context, SurfaceView surfaceView, int cameraId, IDcDecodeListener listener) {
    }

    @Override
    public boolean startScanner() {
        String ret = BasicOper.dc_Scan2DBarcodeStart(0);
        if (ret.startsWith("0000")) {
            flag = true;
            new Thread(() -> {
                while (flag) {
                    String result = BasicOper.dc_Scan2DBarcodeGetData();
                    logger.debug(result);
                    if (result != null && result.startsWith("0000")) {
                        mListener.onDecodeResult(result.split("\\|")[1].getBytes());
                        Log.d("扫码结果result", "" + (result.split("\\|", -1)[1]));
                    }
                }
                BasicOper.dc_Scan2DBarcodeExit();
            }).start();
            return true;
        } else {
            logger.error("dc_Scan2DBarcodeStart : ret = {}", ret);
        }
        return false;
    }

    @Override
    public void stopScanner() {
        flag = false;
    }

    @Override
    public void release() {
        stopScanner();
    }
}
