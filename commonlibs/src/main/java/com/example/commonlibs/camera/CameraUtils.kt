package com.example.commonlibs.camera

import android.content.Context
import android.graphics.Bitmap
import android.view.TextureView

abstract class CameraUtils {

    abstract fun startCamera(tv: TextureView, context: Context)

    abstract fun takePhoto(iPhotoInterface: IPhotoInterface)

    abstract fun turnCamera()

    abstract fun closeCamera()

    abstract fun getBitmap(byteArray: ByteArray?): Bitmap?

    abstract fun getPreviewDatas(): ByteArray?
}