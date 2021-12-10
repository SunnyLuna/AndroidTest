package com.decard.androidtest.net.interceptor

import android.util.Log
import com.example.commonlibs.utils.HexUtils
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import java.net.URLDecoder
import java.security.MessageDigest
import java.util.*

class RequestInterceptor : Interceptor {


    private val TAG = "---RequestInterceptor"
    override fun intercept(chain: Interceptor.Chain): Response {
        val mSecretKey = ""//工作秘钥
        val charset = Charsets.UTF_8
        val originalRequest = chain.request();
        val requestBody = originalRequest.body

        var body: String? = null
        if (requestBody != null) {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            body = buffer.readString(charset)
            Log.d(TAG, "intercept: body:  $body")
        }
        val map = formToMap(body)
        val formString = mapToForm(map)
        Log.d(TAG, "排序后字符串  $formString")
        val originalStr = URLDecoder.decode(formString, "UTF-8")
        Log.d(TAG, "恢复被转义字符  $originalStr")
        val md5 = sign(originalStr, mSecretKey)
        Log.d(TAG, "md5  $md5")
        body += "&signValue=${md5}"
        val newRequest = originalRequest.newBuilder()
            .post(RequestBody.create(originalRequest.body?.contentType(), body!!)).build()

        return chain.proceed(newRequest)
    }

    private fun formToMap(form: String?): TreeMap<String, Any> {
        val map = TreeMap<String, Any>()
        if (form == null) {
            return map;
        }
        val formItems = form.split("&");
        for (item in formItems) {
            val keyValue = item.split("=")
            if (keyValue.size == 2) {
                map.put(keyValue[0], keyValue[1])
            }
        }
        return map
    }

    private fun mapToForm(map: Map<String, Any>): String {
        val stringBuilder = StringBuilder()
        for (key in map.keys) {
            if (map[key] == null || (map[key] as String).trim().length == 0) {
                continue
            }
            if (stringBuilder.isNotEmpty()) {
                stringBuilder.append("&")
            }
            stringBuilder.append(key).append("=").append(map[key])
        }
        return stringBuilder.toString()
    }

    private fun sign(data: String, key: String): String? {
        val string = "$data&key=${key}"
        Log.d(TAG, "sign: 被加签字符串$string")
        //Log.d(TAG, "string  === ${string}")
        //得到明文的字节数组
        val btInput = string.toByteArray(Charsets.UTF_8)
        // 创建一个提供信息摘要算法的对象(MD5摘要算法)
        val messageDigest: MessageDigest = MessageDigest.getInstance("MD5")
        // 使用指定的字节更新摘要
        messageDigest.update(btInput)
        // 得到二进制的密文
        val encryptData: ByteArray = messageDigest.digest()
        // 把密文转换成十六进制的字符串形式
        return HexUtils.toHexString(encryptData)
    }

}