package com.decard.uilibs.md;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;

public class DrawableUtils {
	private static final String TAG = "---DrawableUtils";
	Context mContext;

	public DrawableUtils(Context context) {
		mContext = context;
	}

	public Drawable utils(Drawable drawable) {
		DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
		int width = displayMetrics.widthPixels;
		int height = displayMetrics.heightPixels;

		int draWidth = drawable.getIntrinsicWidth();
		int draHeight = drawable.getIntrinsicHeight();
		Log.d(TAG, "utils: width" + width + "height" + height);
		Log.d(TAG, "utils: draWidth" + draWidth + "draHeight" + draHeight);

		int resWidth = draWidth, resHeight = draHeight;

		if (draWidth < width && draHeight < height) {
			resWidth = (int) (draWidth * 2.5);
			resHeight = (int) (draHeight * 2.5);
		} else if (draWidth > width || draHeight > height) {
			int value = draWidth / width;
			resWidth = draWidth / value;
			resHeight = draHeight / value;
		}
		drawable.setBounds(0, 0, resWidth, resHeight);
		return drawable;
	}
}

