package com.decard.uilibs.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;


public class CustomStar extends View {
	private static final String TAG = "---CustomStar";
	private Path mPath;
	private Paint mPaint;

	public CustomStar(Context context) {
		this(context, null, 0, 0);
	}

	public CustomStar(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0, 0);
	}

	public CustomStar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
	}

	public CustomStar(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
	                  int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	private void init() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPath = new Path();
	}

	int mWidth = 0;
	int mHeight = 0;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidth = MeasureSpec.getSize(widthMeasureSpec);
		mHeight = MeasureSpec.getSize(heightMeasureSpec);
		mWidth = Math.min(mWidth, mHeight);
		Log.d(TAG, "onMeasure: mWidth：" + mWidth + "   mHeight：" + mHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		mPaint.setColor(Color.YELLOW);
//		canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2, mPaint);
//		mPaint.setColor(Color.GRAY);
//		drawStar(canvas, mWidth, 0);
//		canvas.clipRect(0, 0, mWidth / 2, mWidth);
		mPaint.setColor(Color.RED);
		PathEffect pathEffect = new CornerPathEffect(200);
		mPaint.setPathEffect(pathEffect);
		drawStar(canvas, mWidth, 0);
	}


	private void drawStar(Canvas canvas, int width, int count) {
		int radius = width / 2;
		float x1 = (float) width / 2 + (float) count * width;
		float y1 = 0;
		Log.d(TAG, "x1: " + x1 + "  y1: " + y1);
		float x2 =
				(float) (radius + width * Math.cos(degree2Radian(18)) * Math.cos(degree2Radian(72))) + (float) count * width;
		float y2 = (float) (width * Math.cos(degree2Radian(18)) * Math.sin(degree2Radian(72)));
		Log.d(TAG, "x2: " + x2 + "  y2: " + y2);
		float x3 =
				(float) ((width - width * Math.cos(degree2Radian(18))) / 2) + (float) count * width;
		//先算五角星的边长，a=mwidth*cos18/
		float y3 =
				(float) (width * Math.cos(degree2Radian(18)) * Math.cos(degree2Radian(18)) / (2 * (1 + Math.sin(degree2Radian(18)))));
		Log.d(TAG, "x3: " + x3 + "  y3: " + y3);
		float x4 = (float) ((width * Math.cos(degree2Radian(18)) + x3));
		float y4 = y3;
		Log.d(TAG, "x4: " + x4 + "  y4: " + y4);
		float x5 =
				(float) (radius - width * Math.cos(degree2Radian(18)) * Math.cos(degree2Radian(72))) + (float) count * width;
		float y5 = y2;
		Log.d(TAG, "x5: " + x5 + "  y5: " + y5);
		mPath.moveTo(x1, y1);
		mPath.lineTo(x2, y2);
		mPath.lineTo(x3, y3);
		mPath.lineTo(x4, y4);
		mPath.lineTo(x5, y5);
		mPath.close();
		canvas.drawPath(mPath, mPaint);
	}

	private double degree2Radian(int degree) {
		return (double) (Math.PI * degree / 180);
	}
}
