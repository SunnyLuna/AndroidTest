package com.decard.lib.qrcode;

import android.content.Context;
import android.view.SurfaceView;

import com.decard.dcdecodes.utils.IDcDecodeListener;


public class DCSerialPortQRScanner implements QRScanner {

    @Override
    public void initData(String devicePath, int baudRate, IDcDecodeListener listener) {

    }

    @Override
    public void initData(Context context, IDcDecodeListener listener) {

    }

    @Override
    public void initData(Context context, SurfaceView surfaceView, int cameraId, IDcDecodeListener listener) {

    }

    @Override
    public boolean startScanner() {
        return false;
    }

    @Override
    public void stopScanner() {

    }

    @Override
    public void release() {

    }
}
