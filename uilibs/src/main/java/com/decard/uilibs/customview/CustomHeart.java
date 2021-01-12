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
 * 自定义心
 *
 * @author ZJ
 * created at 2020/12/9 15:28
 */
public class CustomHeart extends View {
	private static final String TAG = "---CustomHeart";
	private int DEFAULT_WIDTH = 500;
	private Paint mPaint;
	private Path mPath;
	private float mWidth;
	private float mHeight;

	public CustomHeart(Context context) {
		this(context, null, 0, 0);
	}

	public CustomHeart(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0, 0);
	}

	public CustomHeart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
	}

	public CustomHeart(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
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
		int width = getMySize(DEFAULT_WIDTH, widthMeasureSpec);
		int height = getMySize(DEFAULT_WIDTH, heightMeasureSpec);
		int size = Math.min(width, height);
		mWidth = size;
		mHeight = size ;
		setMeasuredDimension(size, size);
		Log.d(TAG, "onMeasure: width" + mWidth + "   height:" + height);
	}

	private int getMySize(int defaultSize, int measureSpec) {
		int mySize = defaultSize;
		int mode = MeasureSpec.getMode(measureSpec);
		int size = MeasureSpec.getSize(measureSpec);
		Log.d(TAG, "onMeasure: mode" + mode + "  size:" + size);
		switch (mode) {
			//父容器已经测量出子View所需要的大小,即measureSpec中封装的specsize,对应于LayoutParams中的match_parent和设置的固定值
			case MeasureSpec.EXACTLY://如果是固定的大小，那就不要去改变它
				mySize = size;
				break;
			//父窗口限定了一个最大值给子View即SpecSize,对应于LayoutParams中的wrap_content
			case MeasureSpec.AT_MOST://如果测量模式是最大取值为size
				//父容器不对子View有任何限制,子View要多大给多大，有系统内部调用，我们不需要研究，例如ScrollView
			case MeasureSpec.UNSPECIFIED://如果没有指定大小，就设置为默认大小
				//我们将大小取最大值,你也可以取其他值
				mySize = defaultSize;
				break;
		}
		return mySize;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawHeart(canvas);
	}

	private void drawHeart(Canvas canvas) {
		// 绘制心形
		customStarOne(canvas);

	}

	private void customStar(Canvas canvas) {
		mPaint.setColor(Color.RED);
		Path path = new Path();
		path.moveTo(mWidth / 2, mHeight / 4);
		path.cubicTo(mWidth * 1 / 5, 0, 0,
				mHeight * 2 / 5,mWidth * 2 / 5, mHeight * 3 / 5);
		path.quadTo(mWidth * 499/1000, mHeight * 65/ 100, mWidth / 2,
				mHeight*67/100 );
		canvas.drawPath(path, mPaint);

		Path path2 = new Path();
		path2.moveTo(mWidth / 2, mHeight / 4);
		path2.cubicTo(mWidth * 4 / 5, 0, mWidth,
				mHeight * 2 / 5,mWidth * 3 / 5, mHeight * 3 / 5);
		path2.quadTo(mWidth * 501/1000, mHeight * 65/ 100, mWidth / 2,
				mHeight*67/100);
		canvas.drawPath(path2, mPaint);
	}

	private void customStarOne(Canvas canvas) {
		mPaint.setColor(Color.RED);
		Path path = new Path();
		path.moveTo(mWidth / 2, mHeight / 4);
		path.cubicTo(mWidth * 1 / 4, 0, 0,
				mHeight * 2 / 5,mWidth * 1 / 3, mHeight * 3 / 5);
		path.quadTo(mWidth * 19/40, mHeight * 7/10, mWidth / 2,
				mHeight*15/20);
		canvas.drawPath(path, mPaint);

		Path path2 = new Path();
		path2.moveTo(mWidth / 2, mHeight / 4);
		path2.cubicTo(mWidth * 3 / 4, 0, mWidth,
				mHeight * 2 / 5,mWidth * 2/3, mHeight * 3 / 5);
		path2.quadTo(mWidth * 21/40, mHeight * 7/10, mWidth / 2,
				mHeight*15/20);
		canvas.drawPath(path2, mPaint);
	}


	/**
	 * 角度转弧度
	 * 三角函数中均以弧度来进行计算
	 *
	 * @param degree 角度
	 * @return 弧度
	 */
	private double degree2Radian(int degree) {
		return (double) (Math.PI * degree / 180);
	}
}
