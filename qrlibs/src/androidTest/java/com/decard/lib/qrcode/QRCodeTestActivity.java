package com.decard.lib.qrcode;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

public class QRCodeTestActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_test);
        surfaceView = findViewById(R.id.surfaceView);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        QRScannerFactory.getDCCodeQRScanner(this,surfaceView);
        QRScannerFactory.getDCCodeQRScanner().init(this,surfaceView);
        QRScannerFactory.getDCCodeQRScanner().startScanner();
        QRScannerFactory.getDCCodeQRScanner().getQRLiveData().observe(this,observer);

//        QRScannerContext qrScannerContext = new QRScannerContext( (new DCCodeQRScanner(this,surfaceView)));

//        QRScanner qrScanner = new DCCodeQRScanner(this,surfaceView);
//        qrScanner.startScanner();

//        QRScanner qrScanner1 = new DCRF32QRScanner();
//        QRScanner qrScanner2 = new DCSerialPortQRScanner("/dev/ttySHL0");

    }

    Observer<byte[]> observer = bytes -> {

        Log.e("*****************",new String(bytes));

    };


    @Override
    protected void onStop() {
        super.onStop();
        QRScannerFactory.getDCCodeQRScanner().stopScanner();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        QRScannerFactory.getDCCodeQRScanner().release();
    }
}
