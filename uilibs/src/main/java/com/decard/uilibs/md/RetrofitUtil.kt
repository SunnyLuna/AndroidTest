import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * File Description
 * @author Dell
 * @date 2018/9/11
 *
 */
object RetrofitUtil {

    //companion object声明static变量
    /**
     * 创建retrofit
     */
    private fun create(url: String): Retrofit {
        val builder = OkHttpClient().newBuilder()
        builder.readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
        return Retrofit.Builder()
            .baseUrl(url)
            .client(builder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }


    private fun <T> getService(url: String, service: Class<T>): T {
        return create(url).create(service)
    }


    fun getTest(): RetrofitService {
        return getService("http://news-at.zhihu.com/", RetrofitService::class.java)
    }

    private fun createString(url: String): Retrofit {
        val builder = OkHttpClient().newBuilder()
        builder.readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
        return Retrofit.Builder()
            .baseUrl(url)
            .client(builder.build())
//            .addConverterFactory(StringConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun <T> getStringService(url: String, service: Class<T>): T {
        return createString(url).create(service)
    }

    fun getData(): RetrofitService {
        return getStringService("http://news-at.zhihu.com/", RetrofitService::class.java)
    }
}