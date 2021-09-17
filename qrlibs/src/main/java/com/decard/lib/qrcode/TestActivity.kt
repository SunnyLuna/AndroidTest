package com.decard.lib.qrcode

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.decard.dcdecodes.utils.IDcDecodeListener

class TestActivity : AppCompatActivity() {
    private val TAG = "---TestActivity"

    lateinit var dcrF32QRScanner: DCRF32QRScanner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
//        val numberOfCameras = Camera.getNumberOfCameras()
//        Log.d(TAG, "onCreate: $numberOfCameras")
//        val cameraManager =
//            getSystemService(Context.CAMERA_SERVICE) as CameraManager
//        try {
//            for (i in cameraManager.cameraIdList) {
//                Log.d(TAG, "onCreate: $i")
//            }
//        } catch (e: Exception) {
//            Log.d(TAG, ": ${e.message}")
//        }
//        initQR()
//        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.zj)
//        if (bitmap != null) {
//            ZXingUtils.zxingScanQrcodePicture(bitmap)
//        } else {
//            Log.d(TAG, "onCreate: bitmap为空")
//        }
        val imageView = findViewById<ImageView>(R.id.iv_qrcode)

        val filePath = Environment.getExternalStorageDirectory().path + "/barcode.jpg"
//        val filePath = Environment.getExternalStorageDirectory().path + "/qr.jpg"
//        val decodeQrcodePicture = ZXingUtils.decodeQrcodePicture(filePath)
//        Log.d(TAG, "onCreate: $decodeQrcodePicture")

        val logoBitmap = BitmapFactory.decodeResource(resources,R.mipmap.yan)
        val bitmap = ZXingUtils.generateQRBitmap("今天是个好日子",200,200, logoBitmap)

//        val bitmap =ZXingUtils.BarcodeFormatCode("123456789")
        imageView.setImageBitmap(bitmap)
    }

    private fun initQR() {
        Log.d(TAG, "initQR: ")
        val qrScanner = QRScannerFactory.getInstance(2)
//        qrScanner.initData("/dev/ttyHSL1",115200)
        qrScanner.initData(this, object : IDcDecodeListener {
            override fun onActiveResult(s: String?) {
                Log.d(TAG, "onActiveResult: $s")
            }

            override fun onDecodeResult(result: ByteArray?) {
                Log.d(TAG, "onDecodeResult: ${String(result!!)}")
            }

        })
        qrScanner.startScanner()


    }

    override fun onStop() {
        super.onStop()
//        dcrF32QRScanner.stopScanner()
    }
}