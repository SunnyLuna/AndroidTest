package com.decard.lib.qrcode;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.decard.NDKMethod.BasicOper;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class DCRF32QRScannerTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    CountDownLatch latch = null;

    @Before
    public void setUp() throws Exception {
        int ret = BasicOper.dc_open("COM",null,"/dev/ttyS0",115200);
        System.out.println("ret = "+ret);
        assertTrue(ret>0);
    }


    /**
     * 一分钟连扫测试
     */
    @Test
    public void test(){

        boolean ok = QRScannerFactory.getDCRF32QRScanner().init();
        assertTrue(ok);
        QRScannerFactory.getDCRF32QRScanner().getQRLiveData().observeForever(QrObserver);
        ok = QRScannerFactory.getDCRF32QRScanner().startScanner();
        assertTrue(ok);


        final CountDownLatch latch = new CountDownLatch(1);
        try {
            latch.await(60, TimeUnit.SECONDS);
        }catch (InterruptedException e){
            e.printStackTrace();
        }


        QRScannerFactory.getDCRF32QRScanner().getQRLiveData().removeObserver(QrObserver);
    }



    private Observer<byte[]> QrObserver = bytes -> {

        System.out.println(new String(bytes));

    };






    /**
     * 扫一次结束
     */
    @Test
    public void testOnlyOnce(){

        boolean ok = QRScannerFactory.getDCRF32QRScanner().init();
        assertTrue(ok);
        QRScannerFactory.getDCRF32QRScanner().getQRLiveData().observeForever(QrObserverOnce);
        ok = QRScannerFactory.getDCRF32QRScanner().startScanner();
        assertTrue(ok);


        latch = new CountDownLatch(1);
        try {
            latch.await(60, TimeUnit.SECONDS);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        QRScannerFactory.getDCRF32QRScanner().getQRLiveData().removeObserver(QrObserverOnce);
    }


    private Observer<byte[]> QrObserverOnce = bytes -> {

        QRScannerFactory.getDCRF32QRScanner().stopScanner();
//        latch.countDown();
        System.out.println("stopScanner");
        System.out.println(new String(bytes));

    };

    @After
    public void tearDown() throws Exception {
        QRScannerFactory.getDCRF32QRScanner().release();
        BasicOper.dc_exit();
    }
}
