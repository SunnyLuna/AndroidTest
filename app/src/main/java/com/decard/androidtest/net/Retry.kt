package com.decard.androidtest.net

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

/**
 * OKHTTP重试
 * @author ZJ
 * created at 2020/11/16 16:31
 */
public class Retry : Interceptor {
    var maxRetry = 3//最大重试次数
    private var retryNum = 0 //假如设置为3次重试的话，则最大可能请求4次（默认1次+3次重试）
    private val TAG = "---Retry"
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.d(TAG, "intercept: ")
        val request = chain.request()
        var response = chain.proceed(request)
        Log.d(TAG, "response.body: " + response)
        Log.d(TAG, "intercept: ${response.isSuccessful}")
        while (!response.isSuccessful && retryNum < maxRetry) {
            retryNum++
            Log.i(TAG, "num:" + retryNum)
            response = chain.proceed(request)
        }
        return response
    }


}