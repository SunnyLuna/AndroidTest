package com.decard.androidtest.net.factory

import okhttp3.Call
import okhttp3.HttpUrl
import okhttp3.Request
import org.slf4j.LoggerFactory

/**
 *
 * @author ZJ
 * create at 2022/2/11 13:52
 */
abstract class CallFactoryProxy(callFactory: Call.Factory) : Call.Factory {

    private val logger = LoggerFactory.getLogger("---CallFactoryProxy")
    private var callFactory: Call.Factory = callFactory

    override fun newCall(request: Request): Call {
        val baseUrlName = request.header("BaseUrlName")
        logger.debug("newCall: $baseUrlName")
        if (baseUrlName != null) {
            val newUrl = getNewUrl(baseUrlName, request)
            if (newUrl != null) {
                val newRequest = request.newBuilder().url(newUrl).build()
                return callFactory.newCall(newRequest)
            }
        }
        return callFactory.newCall(request)
    }

    abstract fun getNewUrl(baseUrlName: String, request: Request): HttpUrl?
}