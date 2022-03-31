package com.decard.netlibrary.net.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {

    private static RetrofitUtils retrofitUtils = new RetrofitUtils();

    public static RetrofitUtils getInstance() {
        return retrofitUtils;
    }

    public Retrofit create(String url) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(15, TimeUnit.SECONDS);
        builder.connectTimeout(15, TimeUnit.SECONDS);
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private <T> T getService(String url, Class<T> tClass) {
        return create(url).create(tClass);
    }

    public ApiService getData() {
        return getService("http://59.110.162.30/", ApiService.class);
    }
}
