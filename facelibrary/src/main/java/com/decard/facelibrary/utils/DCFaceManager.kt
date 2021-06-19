package com.decard.facelibrary.utils

import android.content.Context

abstract class DCFaceManager {


    abstract fun activeFace(
        context: Context,
        activeKey: String,
        appId: String,
        sdkKey: String
    ): Boolean

    abstract fun initFaceEngine(): Int

    abstract fun recognizeFace(byteArray: ByteArray, width: Int, height: Int): Boolean

    abstract fun compareFace(idByteArray: ByteArray, cameraByteArray: ByteArray): Int

    abstract fun release()
}