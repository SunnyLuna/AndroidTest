package com.decard.androidtest.net.bean

/**
 * 后台接口返回基类
 * @author ZJ
 * created at 2021/7/30 16:48
 */
data class BaseResponse<T>(
    val success: String,
    val resultCode: String,
    val message: String?,
    val time: String,
    val data: T

) {

    override fun toString(): String {
        return "BaseResponse(success='$success', resultCode='$resultCode', message='$message', time='$time', data=${data.toString()})"
    }
};

