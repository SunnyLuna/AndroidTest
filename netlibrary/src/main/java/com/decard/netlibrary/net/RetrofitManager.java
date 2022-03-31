package com.decard.netlibrary.net;

import android.util.Log;

import com.decard.netlibrary.bean.DataBean;
import com.decard.netlibrary.net.retrofit.RetrofitUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class RetrofitManager implements INetManager {

    private static final String TAG = "----RetrofitManager";

    @Override
    public void get(String url, final INetCallback callback) {
        RetrofitUtils.getInstance().getData().checkVersion()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DataBean dataBean) {
                        Log.d(TAG, "onNext: " + dataBean.toString());
                        callback.success(dataBean.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void download(String url, final File targetFile, INetDownloadCallback callback) {
        Log.d(TAG, "download: " + targetFile.getAbsolutePath());
        if (!targetFile.exists()) {
            targetFile.getParentFile().mkdirs();
        }
        RetrofitUtils.getInstance().getData().downloadApk()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<Response<ResponseBody>, Boolean>() {
                    @Override
                    public Boolean apply(Response<ResponseBody> response) throws Exception {
                        return writeToSDCard(response, targetFile);
                    }
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    Log.d(TAG, "accept: 文件下载成功");
                } else {
                    Log.d(TAG, "accept: 文件下载失败");
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d(TAG, "accept: " + throwable.getMessage());
            }
        });
    }

    private Boolean writeToSDCard(Response<ResponseBody> response, File targetFile) {
        InputStream inputStream;
        OutputStream outputStream = null;
        inputStream = response.body().byteStream();

        try {
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
                Log.d(TAG, "onNext: " + (int) (finalCurLen * 1.0f / totalLen * 100));
            }
            targetFile.setExecutable(true, false);
            targetFile.setReadable(true, false);
            targetFile.setWritable(true, false);
            return true;
        } catch (Exception e) {
            Log.d(TAG, "onNext: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
