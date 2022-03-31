package com.decard.netlibrary.net.retrofit;

import com.decard.netlibrary.bean.DataBean;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Streaming;

public interface ApiService {

    @GET("app_updater_version.json")
    Observable<DataBean> checkVersion();

    @Streaming
    @GET("v450_imooc_updater.apk")
    Observable<Response<ResponseBody>> downloadApk();
}
