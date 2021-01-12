package com.decard.uilibs.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomView extends View {

	private Paint mPaint;

	public CustomView(Context context) {
		this(context, null, 0, 0);
	}

	public CustomView(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0, 0);
	}

	public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
	}

	public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
	                  int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	private void init() {
		//修改图形边缘处的像素颜色，从而让图形在肉眼看来具有更加平滑的感觉
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(5);
		mPaint.setColor(Color.YELLOW);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//圆
		canvas.drawCircle(396, 396, 396, mPaint);
		//offset 表示跳过数组的前几个数再开始记坐标  count 表示一共要绘制几个点
		float[] points = {0, 0, 50, 50, 50, 100, 100, 50, 100, 100, 150, 50, 150, 100};
		mPaint.setColor(Color.BLACK);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		//点
		canvas.drawPoints(points, 2, 8, mPaint);
		mPaint.setStyle(Paint.Style.FILL);
		RectF rectF = new RectF(120, 120, 180, 280);
		//椭圆
		canvas.drawOval(rectF, mPaint);
		//线
		mPaint.setColor(Color.WHITE);
		mPaint.setStrokeWidth(7);
		canvas.drawLine(0, 396, 396, 0, mPaint);
		//圆角矩形
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(Color.BLUE);
		canvas.drawRoundRect(150, 50, 250, 100, 20, 20, mPaint);
		//扇形
		mPaint.setColor(Color.RED);
		mPaint.setStyle(Paint.Style.FILL);
		canvas.drawArc(200, 200, 260, 300, -70, 80, true, mPaint);
		canvas.drawArc(200, 200, 260, 300, 20, 90, false, mPaint);
		mPaint.setStyle(Paint.Style.STROKE);
		canvas.drawArc(200, 200, 260, 300, 120, 40, false, mPaint);
		canvas.drawArc(200, 200, 260, 300, 170, 100, true, mPaint);

	}

	private static final String TAG = "---CircleView";

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		Log.d(TAG, "onMeasure: 宽：" + width + "       高：" + height);
	}
}
