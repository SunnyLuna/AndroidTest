package com.example.commonlibs.camera

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.util.Log
import com.example.commonlibs.BaseApplication
import com.example.commonlibs.utils.ToastUtils

/**
 * camera 工厂类
 * @author ZJ
 * created at 2019/5/17 15:56
 */
object CameraFactory {
    private val TAG = "---CameraFactory"
    fun openCamera(version: Int): CameraUtils {
        return if (version == 1) {
            CustomCamera1.instance
        } else {
            CustomCamera2.instance
        }
    }

    /**
     * LEGACY：向后兼容的级别，处于该级别的设备意味着它只支持 Camera1 的功能，不具备任何 Camera2 高级特性。
     * LIMITED：除了支持 Camera1 的基础功能之外，还支持部分 Camera2 高级特性的级别。
     * FULL：支持所有 Camera2 的高级特性。
     * LEVEL_3：新增更多 Camera2 高级特性，例如 YUV 数据的后处理等。
     */
    fun openCamera(): CameraUtils {
        val manager =
            BaseApplication.instance.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val front = manager.getCameraCharacteristics("1")
        val get = front.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)
        val back = manager.getCameraCharacteristics("0")
        val get1 = back.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)
        ToastUtils.toast(BaseApplication.instance, get.toString() + get1.toString())
        Log.d(TAG, "onCreate: $get")
        if (get == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL
            || get == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_3
        ) {
            return CustomCamera2.instance
        }
        return CustomCamera1.instance
    }

}