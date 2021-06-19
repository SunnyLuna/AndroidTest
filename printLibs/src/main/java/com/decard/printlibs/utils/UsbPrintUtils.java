package com.decard.printlibs.utils;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.tx.printlib.UsbPrinter;

import java.util.Map;

public class UsbPrintUtils {

    public static UsbDevice getCorrectDevice(Activity activity) {
        final UsbManager usbMgr = (UsbManager) activity.getSystemService(Context.USB_SERVICE);
        final Map<String, UsbDevice> devMap = usbMgr.getDeviceList();
        for (String name : devMap.keySet()) {
            if (UsbPrinter.checkPrinter(devMap.get(name)))
                return devMap.get(name);
        }
        return null;
    }
}
