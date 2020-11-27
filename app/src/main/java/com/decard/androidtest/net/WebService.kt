package com.decard.androidtest.net

import android.os.Environment
import com.decard.androidtest.bean.ResponseData
import com.decard.androidtest.bean.TestBean
import com.decard.androidtest.net.coroutine.CoroutineCallAdapterFactory
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import java.io.File
import java.util.concurrent.TimeUnit

interface WebService {

    @GET("/login")
    fun loadData(): Deferred<BaseResponse<ResponseData>>

    @GET("/login")
    fun load(): Observable<BaseResponse<ResponseData>>

    @GET("/test")
    fun test(): Observable<Int>

    @Headers("Content-Type: application/json", "Accept: application/json") //需要添加头
    @POST("test/pad")
    fun getPhoto(@Query("param") param: String): Observable<TestBean>

    @Streaming
    @GET("/test/intelligent/getAV")
    fun getAV(): Observable<ResponseBody>

    @Streaming
    @GET
    fun getPDF(@Url url: String): Observable<ResponseBody>

    companion object {
        //        private const val BASE_URL = "http:192.168.1.86:8080"
        private const val BASE_URL = "http://192.168.5.158:8080/"

        fun create(): WebService {
            val logger =
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            val file = File(Environment.getExternalStorageDirectory(), "cache")
            val cacheSize: Long = 10 * 1024 * 1024
            val cache = Cache(file, cacheSize)
            val client = OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(logger)
                .addInterceptor(CacheInterceptor.OfflineCacheInterceptor())
                .addNetworkInterceptor(CacheInterceptor.OnlineCacheInterceptor())
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .addNetworkInterceptor(Retry())
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(WebService::class.java)
        }

        fun createCoroutine(): WebService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
                .build()
                .create(WebService::class.java)
        }

        fun createAV(): WebService {
            val logger =
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(WebService::class.java)
        }
    }
}