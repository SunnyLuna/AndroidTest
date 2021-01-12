package com.decard.uilibs.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
/**
 * reset 保留填充类型，不保留原有数据
 * rewind  与reset刚好相反，保留内部数据结构
 * @author ZJ
 * created at 2020/12/8 9:35
 */
public class CustomPath extends View {

	private static final String TAG = "---CustomPath";

	private Paint mPaint;
	private Path mPath;

	public CustomPath(Context context) {
		this(context, null, 0, 0);
	}

	public CustomPath(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0, 0);
	}

	public CustomPath(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
	}

	public CustomPath(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
	                  int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	private void init() {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPath = new Path();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		Log.d(TAG, "onMeasure: width: " + width + "     height: " + height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPaint.setColor(Color.WHITE);
		canvas.drawLine(0, 100, 800, 100, mPaint);
		canvas.drawLine(0, 200, 800, 200, mPaint);
		canvas.drawLine(0, 300, 800, 300, mPaint);
		canvas.drawLine(0, 400, 800, 400, mPaint);
		canvas.drawLine(0, 500, 800, 500, mPaint);
		canvas.drawLine(0, 600, 800, 600, mPaint);
		canvas.drawLine(0, 700, 800, 700, mPaint);
		canvas.drawLine(0, 800, 800, 800, mPaint);
		canvas.drawLine(0, 900, 800, 900, mPaint);
		canvas.drawLine(0, 1000, 800, 1000, mPaint);
		canvas.drawLine(100, 0, 100, 1088, mPaint);
		canvas.drawLine(200, 0, 200, 1088, mPaint);
		canvas.drawLine(300, 0, 300, 1088, mPaint);
		canvas.drawLine(400, 0, 400, 1088, mPaint);
		canvas.drawLine(500, 0, 500, 1088, mPaint);
		canvas.drawLine(600, 0, 600, 1088, mPaint);
		canvas.drawLine(700, 0, 700, 1088, mPaint);
		canvas.drawLine(800, 0, 800, 1088, mPaint);
		mPaint.setColor(Color.RED);
		mPath.addArc(200, 200, 400, 400, -225, 225);
		mPath.arcTo(400, 200, 600, 400, -180, 225, false);
		mPath.lineTo(400, 542);
		canvas.drawPath(mPath, mPaint);
	}
}
