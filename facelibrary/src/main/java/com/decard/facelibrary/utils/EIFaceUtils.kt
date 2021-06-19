package com.decard.facelibrary.utils

import android.content.Context
import android.util.Log
import com.example.commonlibs.utils.SDCardUtil
import com.wf.FaceRecognizeControl

/**
 * ET人脸算法
 * @author ZJ
 * created at 2021/4/14 10:02
 */
class EIFaceUtils : DCFaceManager() {
    private val TAG = "---EIFaceUtils"
    private lateinit var mControl: FaceRecognizeControl
    private lateinit var mContext: Context

    /**
     * 创建单例模式
     */
    companion object {
        val instance: EIFaceUtils get() = Holder.eiFaceUtils
    }

    //通过object创建内部对象
    private object Holder {
        val eiFaceUtils = EIFaceUtils()
    }

    /**
     * 激活算法
     */
    override fun activeFace(
        context: Context,
        appId: String,
        sdkKey: String,
        activeKey: String
    ): Boolean {
        //将授权文件放到本地
        mContext = context
        SDCardUtil.moveAssetsToSDCard("DevID.ini")
        return true
    }

    override fun initFaceEngine(): Int {
        //初始化人脸
        mControl = FaceRecognizeControl.getInstance(mContext)
        //初始化本地数据库等参数，并获得数据库路径
        val initID = mControl.init()
        Log.d(TAG, "initId数据库路径为：     $initID")
        return 0
    }

    override fun recognizeFace(byteArray: ByteArray, width: Int, height: Int): Boolean {
        // 0 – 当前无操作或释放正在运行的任务 1 – 开启人脸识别 2 – 开启人脸录入
        mControl.state = 1
        mControl.startExecution(byteArray, width, height, null)
        val faceCoordinates = mControl.faceCoordinates
        if (faceCoordinates != null && faceCoordinates.isNotEmpty()) {
            Log.d(TAG, "recognizeFace: 识别到人脸")
            return true
        }
        Log.d(TAG, "recognizeFace: 未识别到人脸")
        return false
    }

    override fun compareFace(idByteArray: ByteArray, cameraByteArray: ByteArray): Int {
        val initID = mControl.initID()
        mControl.setOrientation(270)
        val enrollID = mControl.enrollID(idByteArray)
        if (enrollID == 0) {
            val recognize = mControl.recognizeID(cameraByteArray).toInt()
            Log.d(TAG, "compareFace:人证比对结果 $recognize")
            return recognize
        }
        Log.d(TAG, "compareFace: enrollID注册失败:  $enrollID")
        return enrollID
    }

    override fun release() {

    }


}