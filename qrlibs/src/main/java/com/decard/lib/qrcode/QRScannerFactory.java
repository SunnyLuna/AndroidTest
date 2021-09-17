package com.decard.lib.qrcode;

/**
 * 扫码工厂类
 *
 * @author ZJ
 * created at 2020/7/2 15:16
 */
public class QRScannerFactory {

    /**
     * @param codeType 0 硬扫，1 软扫  2接口扫
     * @return
     */
    public static QRScanner getInstance(int codeType) {


        if (codeType == 0) {
            return DCRF32QRScanner.getInstance();
        } else if (codeType == 2) {
            return DCInterfaceQRScanner.getInstance();
        }
        return DCCodeQRScanner.getInstance();

    }
}
