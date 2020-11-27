package com.decard.serverlibs;

import android.os.Environment;
import android.util.Log;

import com.yanzhenjie.andserver.annotation.GetMapping;
import com.yanzhenjie.andserver.annotation.PostMapping;
import com.yanzhenjie.andserver.annotation.QueryParam;
import com.yanzhenjie.andserver.annotation.RequestMapping;
import com.yanzhenjie.andserver.annotation.RestController;
import com.yanzhenjie.andserver.framework.body.FileBody;
import com.yanzhenjie.andserver.framework.body.StringBody;
import com.yanzhenjie.andserver.http.HttpResponse;
import com.yanzhenjie.andserver.http.ResponseBody;

import java.io.File;

@RestController
@RequestMapping(path = "/test")
public class TestController {

    private static final String TAG = "----TestController";

    @PostMapping(path = "/pad", consumes = "application/json")
    String getParam(@QueryParam(name = "param") String param) {
        Log.d(TAG, "getParam: " + param);
        return "{\"out\":[\"你真棒\"],\"result\":\"0000\"}";
    }

    @GetMapping("/intelligent/getSignPDF")
    void getPdf(HttpResponse response) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Logan.pdf";
        File file = new File(path);
        if (file.exists()) {
            Log.d(TAG, "getSignPDF: 在哩");
            ResponseBody responseBody = new FileBody(file);
            response.setBody(responseBody);
        } else {
            ResponseBody responseBody = new StringBody("{\"out\":[\"文件不存在\"],\"result\":\"-102\"}");
            response.setBody(responseBody);
            Log.d(TAG, "getSignPDF: 不在");
        }
    }

    @GetMapping("/intelligent/getAV")
    void getAV(HttpResponse response) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "TEST.mp4";
        File file = new File(path);
        if (file.exists()) {
            Log.d(TAG, "getAV: 在哩");
            try {
                ResponseBody responseBody = new FileBody(file);
                response.setBody(responseBody);
            } catch (Exception e) {
                Log.d(TAG, "getAV: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            ResponseBody responseBody = new StringBody("{\"out\":[\"文件不存在\"],\"result\":\"-102\"}");
            response.setBody(responseBody);
            Log.d(TAG, "getAV: 不在");
        }

    }
}
