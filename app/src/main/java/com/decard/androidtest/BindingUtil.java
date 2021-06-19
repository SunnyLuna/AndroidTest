package com.decard.androidtest;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.example.commonlibs.utils.BitmapUtils;

/**
 * File Description
 *
 * @author Dell
 * @date 2018/7/13
 */
public class BindingUtil {


	@BindingAdapter("idPicture")
	public static void idPicture(ImageView imageView, String data) {
		if (data != null) {
//			byte[] photos = Base64.decode(data, 0);
//			Bitmap bitmap = BitmapUtils.bytesToBitmap(photos);
			Bitmap bitmap = BitmapUtils.stringToBitmap(data);
			if (bitmap != null)
				imageView.setImageBitmap(bitmap);
		}

	}

}
