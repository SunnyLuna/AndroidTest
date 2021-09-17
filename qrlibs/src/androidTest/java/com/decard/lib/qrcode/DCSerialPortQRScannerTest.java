package com.decard.lib.qrcode;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.decard.NDKMethod.BasicOper;
import com.decard.lib.utils.HexUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DCSerialPortQRScannerTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    @Before
    public void setUp() throws Exception {
        boolean ok = QRScannerFactory.getDCSerialPortQRScanner().init("/dev/ttyHSL0",115200);
        assertTrue(ok);
    }


    @Test
    public void test(){

        QRScannerFactory.getDCSerialPortQRScanner().getQRLiveData().observeForever(QrObserver);
        QRScannerFactory.getDCSerialPortQRScanner().startScanner();

        final CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await(60, TimeUnit.MINUTES);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        QRScannerFactory.getDCSerialPortQRScanner().getQRLiveData().removeObserver(QrObserver);


    }


    private Observer<byte[]> QrObserver = bytes -> {

        System.out.println(new String(bytes));
        System.out.println(HexUtils.toHexString(bytes));
    };



    CountDownLatch latch;
    /**
     * 扫一次结束
     */
    @Test
    public void testOnlyOnce(){

        QRScannerFactory.getDCSerialPortQRScanner().getQRLiveData().observeForever(QrObserverOnlyOnce);
        QRScannerFactory.getDCSerialPortQRScanner().startScanner();

        latch = new CountDownLatch(1);
        try {
            latch.await(60, TimeUnit.SECONDS);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        QRScannerFactory.getDCSerialPortQRScanner().getQRLiveData().removeObserver(QrObserverOnlyOnce);
    }

    private Observer<byte[]> QrObserverOnlyOnce = bytes -> {
        latch.countDown();
        System.out.println(new String(bytes));
        System.out.println(HexUtils.toHexString(bytes));
        QRScannerFactory.getDCSerialPortQRScanner().stopScanner();
    };



    @After
    public void tearDown() throws Exception {
        QRScannerFactory.getDCSerialPortQRScanner().release();
        BasicOper.dc_exit();
    }

}
