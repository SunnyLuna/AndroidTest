package com.decard.facelibrary.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.os.SystemClock
import android.util.Log
import com.arcsoft.face.*
import com.arcsoft.face.enums.DetectFaceOrientPriority
import com.arcsoft.face.enums.DetectMode
import com.arcsoft.imageutil.ArcSoftImageFormat
import com.arcsoft.imageutil.ArcSoftImageUtil
import com.arcsoft.imageutil.ArcSoftImageUtilError
import org.slf4j.LoggerFactory


class HRFaceUtils : DCFaceManager() {
    private val TAG = "---HRFaceUtils"
    private val logger = LoggerFactory.getLogger("---HRFaceUtils")
    private lateinit var mContext: Context
    private val livenessInfoList: List<LivenessInfo> = ArrayList()
    private var mFaceEngine: FaceEngine? = null
    private var idFaceEngine: FaceEngine? = null

    /**
     * 创建单例模式
     */
    companion object {
        val instance: HRFaceUtils = Holder.hrFaceUtils
    }

    //通过object创建内部对象
    private object Holder {
        val hrFaceUtils = HRFaceUtils()
    }

    override fun activeFace(
        context: Context,
        activeKey: String,
        appId: String,
        sdkKey: String
    ): Boolean {
        mContext = context
        val activeOnline = FaceEngine.activeOnline(
            context,
            activeKey,
            appId,
            sdkKey
        )
        if (activeOnline == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED || activeOnline == ErrorInfo.MOK) {
            logger.debug("activeFace：激活成功")
            return true
        }
        logger.debug("activeFace: 激活失败   $activeOnline")
        return false
    }


    override fun initFaceEngine(): Int {
        if (mFaceEngine == null) {
            mFaceEngine = FaceEngine()
            //1.上下文 2.VIDEO模式：处理连续帧的图像数据 IMAGE模式：处理单张的图像数据
            //3.人脸检测角度，推荐单一检测角度
            //4.识别的最小人脸比例（图片长边与人脸框长边的比值） VIDEO模式取值范围[2,32]，推荐值为16 IMAGE模式取值范围[2,32]，推荐值为32
            //5.最大需要检测的人脸个数，取值范围[1,50]
            //6.需要启用的功能组合，可多选
            val afCode = mFaceEngine!!.init(
                mContext,
                DetectMode.ASF_DETECT_MODE_VIDEO,
                DetectFaceOrientPriority.ASF_OP_ALL_OUT,
                16,
                1,
                FaceEngine.ASF_FACE_RECOGNITION
                        or FaceEngine.ASF_FACE_DETECT
                        or FaceEngine.ASF_LIVENESS
                        or FaceEngine.ASF_IMAGEQUALITY
            )
            logger.debug("initFaceEngine: $afCode")
            return afCode
        }

        return 0
    }

    override fun recognizeFace(bytes: ByteArray, width: Int, height: Int): Boolean {
        Log.d(
            TAG,
            "recognizeFace: ****************************************************$width  $height"
        )
        Log.d(TAG, "recognizeFace: $bytes ${Thread.currentThread().name}")
        val faceInfoList: List<FaceInfo> = ArrayList()
        val detectFaces = mFaceEngine?.detectFaces(
            bytes,
            width,
            height,
            FaceEngine.CP_PAF_NV21,
            faceInfoList
        )
        Log.d(TAG, "recognizeFace: detectFaces:$detectFaces   faceInfoList:${faceInfoList.size}")
        if (detectFaces == ErrorInfo.MOK && faceInfoList.isNotEmpty()) {
            Log.d(TAG, "recognizeFace: 视频流检测到人脸数量${faceInfoList.size}")
            //TODO Step2  图像质量检
            val imageQualityList: List<Float> = ArrayList()
            // 对人脸信息进行图像质量检测
            val QualityTims = System.currentTimeMillis()
            val imageQualityDetectCode = mFaceEngine?.imageQualityDetect(
                bytes,
                width,
                height,
                FaceEngine.CP_PAF_NV21,
                faceInfoList,
                imageQualityList
            )
            val qualityStime = System.currentTimeMillis() - QualityTims
            Log.d(TAG, "recognizeFace: 图像质量检测耗时:$qualityStime")
            if (imageQualityDetectCode == ErrorInfo.MOK) {
                for (i in imageQualityList.indices) {
                    Log.i(
                        TAG,
                        "=========视频流质量检测通过，第[" + i + "]个人脸质量: " + imageQualityList[i]
                    )
                }
                //TODO Step3 活体检测
                val LivenessTims = System.currentTimeMillis()
                val process = mFaceEngine?.process(
                    bytes,
                    width,
                    height,
                    FaceEngine.CP_PAF_NV21,
                    faceInfoList,
                    FaceEngine.ASF_LIVENESS
                )
                Log.d(TAG, "recognizeFace: process执行结果$process")
                val code = mFaceEngine?.getLiveness(livenessInfoList)
                val liveSpaceTime = System.currentTimeMillis() - LivenessTims
                Log.d(TAG, "活体检测耗时:$liveSpaceTime")
                Log.d(TAG, "recognizeFace: 活体检测接口执行结果:$code")
                for (i in livenessInfoList.indices) {
                    val livenessInfo = livenessInfoList[i]
                    val livenessCode = livenessInfo.liveness
                    Log.d(TAG, "recognizeFace: 视频流活体检测结果(1:活体   0:非活体):$livenessCode")
                    if (livenessCode == 1) {
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun compareFace(idByteArray: ByteArray, cameraByteArray: ByteArray): Int {

        Log.d(TAG, "compareFace: **************************************************************")
        Log.d(TAG, "compareFace: $cameraByteArray")
        //************获取相机特征值
        val cameraFaceInfoList: List<FaceInfo> = ArrayList()
        //检测照相机人脸
        val cameraDetectFaces = mFaceEngine?.detectFaces(
            cameraByteArray,
            640,
            480,
            FaceEngine.CP_PAF_NV21,
            cameraFaceInfoList
        )
        Log.d(
            TAG,
            "compareFace: cameraDetectFaces：$cameraDetectFaces  cameraFaceInfoList:${cameraFaceInfoList.size}"
        )
        val cameraFaceFeature = FaceFeature()
        if (cameraDetectFaces == ErrorInfo.MOK && cameraFaceInfoList.isNotEmpty()) {
            //在FaceFeature的二进制数组中保存获取到的人脸特征数据
            val cameraExtractFaceFeature = mFaceEngine?.extractFaceFeature(
                cameraByteArray, 640,
                480, FaceEngine.CP_PAF_NV21, cameraFaceInfoList[0], cameraFaceFeature
            )
            Log.d(TAG, "compareFace: cameraExtractFaceFeature：$cameraExtractFaceFeature")
        }
        //************获取照片特征值
        if (idFaceEngine == null) {
            idFaceEngine = FaceEngine()
            //1.上下文 2.VIDEO模式：处理连续帧的图像数据 IMAGE模式：处理单张的图像数据
            //3.人脸检测角度，推荐单一检测角度
            //4.识别的最小人脸比例（图片长边与人脸框长边的比值） VIDEO模式取值范围[2,32]，推荐值为16 IMAGE模式取值范围[2,32]，推荐值为32
            //5.最大需要检测的人脸个数，取值范围[1,50]
            //6.需要启用的功能组合，可多选
            val afCode = idFaceEngine?.init(
                mContext,
                DetectMode.ASF_DETECT_MODE_IMAGE,
                DetectFaceOrientPriority.ASF_OP_ALL_OUT,
                32,
                1,
                FaceEngine.ASF_FACE_RECOGNITION
                        or FaceEngine.ASF_FACE_DETECT
            )
            Log.d(TAG, "compareFace: id人脸引擎afCode: " + afCode)
        }

        val originalBitmap = BitmapFactory.decodeByteArray(idByteArray, 0, idByteArray.size)
        //获取宽高符合要求的图像
        val bitmap = ArcSoftImageUtil.getAlignedBitmap(originalBitmap, true)
        // 为图像数据分配内存
        val bgr24 =
            ArcSoftImageUtil.createImageData(bitmap.width, bitmap.height, ArcSoftImageFormat.BGR24)
        // 图像格式转换
        val transformCode = ArcSoftImageUtil.bitmapToImageData(
            bitmap, bgr24,
            ArcSoftImageFormat.BGR24
        )
        Log.d(TAG, "compareFace: 图像格式转换transformCode: $transformCode")
        Log.d(TAG, "compareFace: 图像宽: ${originalBitmap.width}  图像高: ${originalBitmap.height}")
        Log.d(TAG, "compareFace: 图像宽: ${bitmap.width}  图像高: ${bitmap.height}")
        if (transformCode != ArcSoftImageUtilError.CODE_SUCCESS) {
            return -1
        }
        val idFaceInfoList: List<FaceInfo> = ArrayList()
        //检测身份证人脸
        val idDetectFaces = idFaceEngine?.detectFaces(
            bgr24,
            bitmap.width,
            bitmap.height,
            FaceEngine.CP_PAF_BGR24,
            idFaceInfoList
        )
        Log.d(
            TAG,
            "compareFace: idDetectFaces：$idDetectFaces  idFaceInfoList: ${idFaceInfoList.size}"
        )
        val idFaceFeature = FaceFeature()
        if (idDetectFaces == ErrorInfo.MOK && idFaceInfoList.isNotEmpty()) {
            //在FaceFeature的二进制数组中保存获取到的人脸特征数据
            val idExtractFaceFeature = idFaceEngine?.extractFaceFeature(
                bgr24, bitmap.width,
                bitmap.height, FaceEngine.CP_PAF_BGR24, idFaceInfoList[0], idFaceFeature
            )
            Log.d(TAG, "compareFace: idExtractFaceFeature：$idExtractFaceFeature")
        }

        val faceSimilar = FaceSimilar()
        val compareFaceFeature =
            mFaceEngine?.compareFaceFeature(idFaceFeature, cameraFaceFeature, faceSimilar)
        Log.d(TAG, "compareFaceFeature: $compareFaceFeature")
        if (compareFaceFeature == ErrorInfo.MOK) {
            val score = faceSimilar.score * 100
            Log.d(TAG, "compareFace: $score")
            return score.toInt()
        }
        return 0
    }

    override fun release() {
        mFaceEngine?.unInit()
        idFaceEngine?.unInit()
        mFaceEngine = null
        idFaceEngine = null
    }
}