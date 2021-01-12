package com.decard.androidtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.example.commonlibs.utils.BitmapUtils;

import java.io.File;

/**
 * File Description
 *
 * @author Dell
 * @date 2018/7/13
 */
public class BindingUtil {

    @BindingAdapter("photoSrc")
    public static void showImg(ImageView imageView, Bitmap bitmap) {
        if (bitmap != null)
            imageView.setImageBitmap(bitmap);
    }

    @BindingAdapter("showPhoto")
    public static void showImg(ImageView imageView, String data) {
        if (data!=null){
            byte[] photos = Base64.decode(data, 0);
            Bitmap bitmap = BitmapUtils.bytesToBitmap(photos);
            if (bitmap != null)
                imageView.setImageBitmap(bitmap);
        }

    }


    @BindingAdapter({"photo"})
    public static void loadImg(ImageView imageView, String path) {
        if (path != null) {
            File mFile = new File(path);
            //若该文件存在
            if (mFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                imageView.setImageBitmap(bitmap);
            }
        } else {
//            imageView.setImageResource(R.mipmap.padding_default);
        }
    }
}
