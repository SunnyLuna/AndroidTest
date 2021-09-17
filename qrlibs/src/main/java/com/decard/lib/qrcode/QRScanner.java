package com.decard.lib.qrcode;

import android.content.Context;
import android.view.SurfaceView;

import com.decard.dcdecodes.utils.IDcDecodeListener;

/**
 * 定义扫码接口
 *
 * @author ZJ
 * created at 2020/7/2 15:09
 */
public interface QRScanner {

    void initData(String devicePath, int baudRate, IDcDecodeListener listener);

    void initData(Context context, IDcDecodeListener listener);

    void initData(Context context, SurfaceView surfaceView, int cameraId, IDcDecodeListener listener);

    boolean startScanner();

    void stopScanner();

    void release();

}
