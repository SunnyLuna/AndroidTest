package com.decard.androidtest.ui

import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.decard.androidtest.R
import com.decard.facelibrary.DCFaceFactory
import com.decard.facelibrary.utils.DCFaceManager
import com.example.commonlibs.camera.CameraFactory
import com.example.commonlibs.camera.CameraUtils
import com.example.commonlibs.utils.BitmapUtils
import com.example.commonlibs.utils.YUVUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_face.*
import java.io.ByteArrayOutputStream

class FaceActivity : AppCompatActivity() {

    private val TAG = "---FaceActivity"
    lateinit var cameraUtils: CameraUtils
    lateinit var faceManager: DCFaceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_face)
        RxPermissions(this).request(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe {
            cameraUtils = CameraFactory.openCamera(1)
            cameraUtils.startCamera(mytexture, this)
            faceManager = DCFaceFactory.initFace(2)
            collectFace(1)
        }
    }

    /**
     * 人脸采集
     */
    private fun collectFace(cameraId: Int) {
        val activeFace = faceManager.activeFace(
            this,
            "085T-1137-W29P-EKEK",
            "2t5HUtnH9fu5XrQgpLF9AiNKr5CAC5Nxk5DVrngibxeY",
            "HwhNdSspXC648MkcgQqGVJ3BLEDRQQ9Yi8iEVVoV5HTE"
        )
        if (!activeFace) {
            Log.d(TAG, "collectFace: 激活失败$activeFace")
            return
        }
        val initFaceEngine = faceManager.initFaceEngine()
        if (initFaceEngine != 0) {
            Log.d(TAG, "collectFace: 初始化引擎失败$initFaceEngine")
            return
        }
        val deviceName = Build.MODEL
        val isCompare = true
        var isResult = false
        Thread {
            var startTime = System.currentTimeMillis()
            do {
                val datas = cameraUtils.getPreviewDatas()
                if (System.currentTimeMillis() - startTime > 5000) {
                    Log.d(TAG, datas.toString())
                    startTime = System.currentTimeMillis()
                }
                if (datas != null) {
                    val isFace = if (deviceName == "SRS1006U") {
                        faceManager.recognizeFace(datas, 480, 640)
                    } else {
                        faceManager.recognizeFace(datas, 640, 480)
                    }
                    if (isFace) {
                        isResult = true
                        Log.d(TAG, "是这样的")
                        val stream = ByteArrayOutputStream()
                        if (cameraId == 2) {
                            val ret = ByteArray(460800)
                            YUVUtils.NV21toI420SemiPlanar(datas, ret, 640, 480)
                            val yuvImage = YuvImage(ret, ImageFormat.NV21, 640, 480, null)
                            yuvImage.compressToJpeg(Rect(0, 0, 640, 480), 100, stream)
                        } else {
                            if (deviceName == "SRS1006U") {
                                val image = YuvImage(datas, ImageFormat.NV21, 480, 640, null)
                                image.compressToJpeg(Rect(0, 0, 480, 640), 100, stream)
                            } else {
                                val image = YuvImage(datas, ImageFormat.NV21, 640, 480, null)
                                image.compressToJpeg(Rect(0, 0, 640, 480), 100, stream)
                            }
                        }
                        if (isCompare) {
                            val zjBitmap = BitmapFactory.decodeResource(resources, R.mipmap.zj)
                            Log.d(TAG, "collectFace: 宽：${zjBitmap.width}  高：${zjBitmap.height}")
                            runOnUiThread {
                                val decodeByteArray =
                                    BitmapFactory.decodeByteArray(
                                        BitmapUtils.bitmapToBytes(zjBitmap)!!,
                                        0,
                                        BitmapUtils.bitmapToBytes(zjBitmap)!!.size
                                    )
                                iv_picture.setImageBitmap(decodeByteArray)
                            }

//                            faceManager.compareFace(
//                                BitmapUtils.bitmapToBytes(zjBitmap)!!,
//                                datas
//                            )
                            faceManager.compareFace(
                                BitmapUtils.bitmapToBytes(zjBitmap)!!,
                                stream.toByteArray()
                            )
                        }
                    }
                }
            } while (!isResult)
        }.start()
    }
}