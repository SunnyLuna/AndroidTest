package com.decard.androidtest.net

import android.os.Environment
import com.decard.androidtest.bean.ResponseData
import com.decard.androidtest.bean.TestBean
import com.decard.androidtest.net.bean.BaseResponse
import com.decard.androidtest.net.bean.response.SignInResponse
import com.decard.androidtest.net.coroutine.CoroutineCallAdapterFactory
import com.decard.androidtest.net.factory.CallFactoryProxy
import com.decard.androidtest.net.interceptor.RequestInterceptor
import com.decard.androidtest.net.interceptor.Retry
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.LoggerFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*
import retrofit2.http.Headers
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * 网络操作类
 * @author ZJ
 * created at 2020/12/10 11:51
 *
 *   @Headers和@Header的区别
 * @Headers用于添加固定请求头，可以同时添加多个，通过该注解添加的请求头不会相互覆盖，而是共同存在。
 * @Header 作为方法的参数传入，用于添加不固定的请求头，该注解会更新已有的请求头
 *
 *      @Field和 @FieldMap 与FormUrlEncoded配合
 *  @FieldMap 的接收类型是Map<String,String>,非String类型会调用其ToString方法

 *    @Part 和 @PartMap  与 Multipart配合 适合有文件上传的情况
 *  @PartMap 的默认接收类型是Map<String,RequestBody>,非RequestBody对象会通过Converter转换
 */
interface WebService {

    //路径前添加“/"代表绝对路径，会忽略baseurl中的路径参数
    @GET("/login")
    fun loadDataAsync(): Deferred<BaseResponse<ResponseData>>

    @Headers("Content-Type: application/json", "Accept: application/json") //需要添加头
    @GET("test/pad")
    fun getPhoto(@Query("param") param: String): Observable<TestBean>

    @FormUrlEncoded //
    @POST("CCBIS/B2CMainPlat_00_ENPAY")
    fun payForm(
        @Field("MERCHANTID") merchantId: String?,
        @Field("BRANCHID") branchId: String?
    ): Observable<TestBean>

    //更新升级状态
    @FormUrlEncoded
    @POST("hbs/{version}/terminal/upgrade/updateStatue")
    fun updateUploadStatus(
        @Path("version") version: String,
        @FieldMap map: Map<String, String>
    ): Observable<BaseResponse<TestBean>>


    @Streaming
    @GET("/test/intelligent/getAV")
    fun getAV(): Observable<ResponseBody>


    @Multipart
    @POST("/fileUpload")
    fun uploadOneFile(
        @Part part: MultipartBody.Part?
    ): Observable<ResponseBody>

    @Multipart
    @POST("/multifileUpload")
    fun uploadMulOne(@PartMap map: MutableMap<String, RequestBody>): Observable<ResponseBody>

    @Multipart
    @POST("/multifileUpload")
    fun uploadMulTwo(@Part map: List<MultipartBody.Part?>): Observable<ResponseBody>

    @Multipart
    @POST("/multifileUpload")
    fun uploadMulTwo(
        @Part("md5") body: RequestBody,
        @Part map: List<MultipartBody.Part?>
    ): Observable<ResponseBody>


    @Streaming
    @GET("/download")
    fun downloadMP4(): Observable<ResponseBody>

    //下载文件
    @Streaming
    @GET
    fun downloadFile(@Url url: String): Observable<ResponseBody>


    //获取令牌
    @FormUrlEncoded
    @POST("arrange/http-sign_in.html")
    fun signIn(
        @FieldMap signInMap: Map<String, String>
    ): Observable<BaseResponse<SignInResponse>>

    /*
    *动态修改baseurl
    */
    @Headers("BaseUrlName:face")
    @POST("faceLab/init")
    @FormUrlEncoded
    fun faceInit()
            : Observable<String>


    companion object {
        private const val BASE_URL = "http:192.168.1.139:8080"

        //        private const val BASE_URL = "http://192.168.5.158:8080/"
        private val logger = LoggerFactory.getLogger("---WebService")

        /**
         * 日志拦截器
         */
        class HttpLogger : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                logger.debug("httpLog: $message")
            }

        }

        fun create(): WebService {

            val logInterceptor = HttpLoggingInterceptor(HttpLogger())
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
            val file = File(Environment.getExternalStorageDirectory(), "cache")
            val cacheSize: Long = 10 * 1024 * 1024
            val cache = Cache(file, cacheSize)
            val client = OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(logInterceptor)
                .addNetworkInterceptor(Retry())
                .addInterceptor(CacheInterceptor.OfflineCacheInterceptor())
                .addNetworkInterceptor(CacheInterceptor.OnlineCacheInterceptor())
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
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

        fun createVerifyService(): WebService {
            val logInterceptor = HttpLoggingInterceptor(HttpLogger())
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)

            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(RequestInterceptor())
                .addInterceptor(logInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .callFactory(object : CallFactoryProxy(client) {
                    override fun getNewUrl(baseUrlName: String, request: Request): HttpUrl? {
                        logger.debug("getNewUrl: $baseUrlName")
                        if (baseUrlName == "face") {
                            val oldUrl = request.url.toString()
                            val newUrl = oldUrl.replace(BASE_URL, "new$BASE_URL")
                            return newUrl.toHttpUrl()
                        }
                        return null
                    }
                })
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(WebService::class.java)
        }
    }
}