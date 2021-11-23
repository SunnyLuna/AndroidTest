package com.decard.androidtest.net.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 *
 * @author ZJ
 * create at 2021/9/17 18:32
 */
class AppInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }
}