package com.decard.androidtest.net.bean.response

/**
 * 签到  出参
 * @author ZJ
 * created at 2021/7/29 11:00
 */
data class SignInResponse(
    val code: String?,
    val message: String?,
    val hospital: String?,
    val deptName: String?,
    val roomName: String?,
    val doctorName: String?,
    val patientName: String?,
    val visitSeq: String?,
    val signTime: String?,
    val waitNum: String?,
)