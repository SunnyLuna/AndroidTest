package com.decard.uilibs.md;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UrlImageGetter implements Html.ImageGetter {

    private Context mContext;

    public UrlImageGetter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public Drawable getDrawable(String source) {
        try {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(source).build();
            Call call = client.newCall(request);
            Response response = call.execute();
            Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
            Drawable drawable = new BitmapDrawable(bitmap);
            DrawableUtils drawableUtils = new DrawableUtils(mContext);
            //调整图片大小
            drawable = drawableUtils.utils(drawable);
            return drawable;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
