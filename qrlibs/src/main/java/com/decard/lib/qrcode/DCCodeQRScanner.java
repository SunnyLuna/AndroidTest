package com.decard.lib.qrcode;

import android.content.Context;
import android.view.SurfaceView;

import androidx.lifecycle.MutableLiveData;

import com.decard.dcdecodes.utils.DCCodeInterface;
import com.decard.dcdecodes.utils.IDcDecodeListener;

/**
 * 软扫
 *
 * @author ZJ
 * created at 2020/7/6 10:04
 */
public class DCCodeQRScanner implements QRScanner {

    public DCCodeQRScanner() {
    }

    private static class Holder {
        private static DCCodeQRScanner instance = new DCCodeQRScanner();
    }

    public static DCCodeQRScanner getInstance() {
        return Holder.instance;
    }


    @Override
    public void initData(String devicePath, int baudRate, IDcDecodeListener listener) {
    }

    @Override
    public void initData(Context context, IDcDecodeListener listener) {

    }

    @Override
    public void initData(Context context, SurfaceView surfaceView, int cameraId, IDcDecodeListener listener) {
        DCCodeInterface.getInstance().init(context, surfaceView, false, 1, cameraId, listener);
    }

    @Override
    public boolean startScanner() {
        DCCodeInterface.getInstance().startScan();
        return false;
    }

    @Override
    public void stopScanner() {
        DCCodeInterface.getInstance().stopScan();
    }

    @Override
    public void release() {
        DCCodeInterface.getInstance().release();
    }
}
