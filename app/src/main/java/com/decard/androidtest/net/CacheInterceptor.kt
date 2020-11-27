package com.decard.androidtest.net

import android.util.Log
import com.example.commonlibs.net.NetUtils
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 缓存拦截器
 * @author ZJ
 * created at 2020/11/18 9:37
 * 应用拦截器
 *不能操作中间的响应结果，比如重定向和重试，只能操作客户端主动的第一次请求以及最终的响应结果。
 *始终调用一次，即使Http响应是从缓存中提供的。
 *关注原始的request，而不关心注入的headers，比如If-None-Match。
 *允许短路 short-circuit ，并且不调用 chain.proceed()。（注：这句话的意思是Chain.proceed()不需要一定要获取来自服务器的响应，但是必须还是需要返回Respond实例。那么实例从哪里来？答案是缓存。如果本地有缓存，可以从本地缓存中获取响应实例返回给客户端。这就是short-circuit (短路)的意思）
 *允许请求失败重试，并多次调用 chain.proceed();

 *网络拦截器
 *能够对重定向和重试等中间响应进行操作
 *不允许调用缓存来short-circuit (短路)这个请求。（注：意思就是说不能从缓存池中获取缓存对象返回给客户端，必须通过请求服务的方式获取响应，也就是Chain.proceed()）
 *观察网络传输中数据传输和变化（注：比如当发生了重定向时，我们就能通过网络拦截器来确定存在重定向的情况）
 *可以获取 Connection 携带的请求信息（即可以通过chain.connection() 获取非空对象）
 */
class CacheInterceptor {
    private val TAG = "---CacheInterceptor"

    /**
     *无网状态下的缓存
     * @author ZJ
     * created at 2020/11/18 11:10
     */
    class OfflineCacheInterceptor : Interceptor {
        private val TAG = "---offlineCache"
        override fun intercept(chain: Interceptor.Chain): Response {
            var request = chain.request()
            val method = request.method
            Log.d(TAG, "intercept: method $method")
            if (!NetUtils.isNetworkAvailable() && method == "GET") {
                val offlineCatchTime = 24 * 60 * 60
                Log.d(TAG, "intercept: $offlineCatchTime")
                request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=$offlineCatchTime")
                    .build()
            }
            return chain.proceed(request)
        }

    }

    /**
     * 有网状态下的缓存
     * 防止用户暴力刷新以减轻服务器压力
     * @author ZJ
     * created at 2020/11/18 11:11
     */
    class OnlineCacheInterceptor : Interceptor {
        private val TAG = "---onlineCache"
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val response = chain.proceed(request)
            Log.d(TAG, "intercept: ")
            // 在线的时候的缓存过期时间，如果想要不缓存，直接时间设置为0
            val onlineCacheTime = 10
            return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, max-age=$onlineCacheTime")
                .build()
        }
    }
}