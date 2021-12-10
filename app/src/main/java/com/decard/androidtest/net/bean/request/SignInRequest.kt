package com.decard.androidtest.net.bean.request

/**
 * 签到  入参
 * @author ZJ
 * created at 2021/7/29 13:56
 */
data class SignInRequest(
    val ip: String,
    val Card_bar_code: String,
    val type: String,
    val Detp_code: String
) {
    fun toMap(): Map<String, String> {
        return mapOf(
            "ip" to ip,
            "Card_bar_code" to Card_bar_code,
            "type" to type,
            "Detp_code" to Detp_code,
        )
    }
}