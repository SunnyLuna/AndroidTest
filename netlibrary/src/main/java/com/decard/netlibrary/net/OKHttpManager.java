package com.decard.netlibrary.net;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OKHttpManager implements INetManager {

    private static OkHttpClient okHttpClient;

    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static final String TAG = "---OKHttpManager";

    static {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.SECONDS);
        okHttpClient = builder.build();
        //https 自签名 okhttp握手的错误
        //builder.sslSocketFactory()
    }

    @Override
    public void get(String url, final INetCallback callback) {
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).get().build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.failed(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            callback.success(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

    @Override
    public void download(String url, final File targetFile, final INetDownloadCallback callback) {
        Log.d(TAG, "download: " + url);
        if (!targetFile.exists()) {
            targetFile.getParentFile().mkdirs();
        }
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).get().build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.failed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                InputStream inputStream;
                OutputStream outputStream;
                inputStream = response.body().byteStream();
                outputStream = new FileOutputStream(targetFile);
                final long totalLen = response.body().contentLength();
                byte[] buffer = new byte[8 * 1024];
                long curLen = 0;
                int bufferLen = 0;
                while ((bufferLen = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bufferLen);
                    outputStream.flush();
                    curLen += bufferLen;
                    final long finalCurLen = curLen;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.progress((int) (finalCurLen * 1.0f / totalLen * 100));
                        }
                    });
                }
                try {
                    targetFile.setExecutable(true, false);
                    targetFile.setReadable(true, false);
                    targetFile.setWritable(true, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.success(targetFile);
                    }
                });
            }
        });

    }


}
