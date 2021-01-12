package com.decard.uilibs.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.decard.uilibs.R;

/**
 * 自定义评价器
 *
 * @author ZJ
 * created at 2020/12/9 9:21
 * 1.自定义五角星，绘制顺序   顶点---右下---左---右---左下
 */
public class CustomEvaluator extends View {
	private static final String TAG = "---CustomStar";
	private int DEFAULT_WIDTH = 200;
	private Path mPath;
	private Paint mPaint;
	private int mStarNum;
	private int mSelectNum;
	private float mRadius;
	private int mWidth = 0;
	private int mHeight = 0;
	private int mSelectColor;
	private int mDefaultColor;
	private OnSelectStarListener mOnSelectStarListener;

	public CustomEvaluator(Context context) {
		this(context, null, 0, 0);
	}

	public CustomEvaluator(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0, 0);
	}

	public CustomEvaluator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
	}

	public CustomEvaluator(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
	                       int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEvaluator,
				defStyleAttr, defStyleRes);
		mStarNum = typedArray.getInt(R.styleable.CustomEvaluator_starNum, 5);
		mSelectNum = typedArray.getInt(R.styleable.CustomEvaluator_selectStar, 3);
		mRadius = typedArray.getFloat(R.styleable.CustomEvaluator_radius, 0);
		mSelectColor = typedArray.getColor(R.styleable.CustomEvaluator_selectColor, Color.RED);
		mDefaultColor = typedArray.getColor(R.styleable.CustomEvaluator_defaultColor,
				Color.LTGRAY);
		typedArray.recycle();
		if (mOnSelectStarListener != null) {
			mOnSelectStarListener.onSelectStar(mSelectNum);
		}
		init();
	}

	private void init() {
		Log.d(TAG, "init: ");
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPath = new Path();
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mWidth = getMySize(DEFAULT_WIDTH, widthMeasureSpec);
		mHeight = getMySize(mWidth / mStarNum, heightMeasureSpec);
		setMeasuredDimension(mWidth, mHeight);
		Log.d(TAG, "onMeasure: mWidth：" + mWidth + "   mHeight：" + mHeight);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		Log.d(TAG, "onSizeChanged: w: " + w + "  h:" + h + "  oldw:" + oldw + "   oldh:" + oldh);
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
		//给图形的轮廓设置效果， CornerPathEffect 把所有拐角变成圆角
		PathEffect pathEffect = new CornerPathEffect(mRadius);
		mPaint.setPathEffect(pathEffect);
		for (int i = 0; i < mStarNum; i++) {
			if (i < mSelectNum) {
				drawStar(canvas, mWidth / mStarNum, i, mSelectColor);
			} else {
				drawStar(canvas, mWidth / mStarNum, i, mDefaultColor);
			}
		}
	}

	/**
	 * 自定义五角星
	 *
	 * @param canvas 画布
	 * @param width  当前view的宽度
	 * @param count  第几颗星
	 * @param color  星的颜色
	 */
	private void drawStar(Canvas canvas, int width, int count, int color) {
		mPath.reset();//保留填充类型，不保留原有数据
		mPaint.setColor(color);
		int radius = width / 2;
		float x1 = (float) width / 2 + (float) count * width;
		float y1 = 0;
//		Log.d(TAG, "x1: " + x1 + "  y1: " + y1);
		float x2 =
				(float) (radius + width * Math.cos(degree2Radian(18)) * Math.cos(degree2Radian(72))) + (float) count * width;
		float y2 = (float) (width * Math.cos(degree2Radian(18)) * Math.sin(degree2Radian(72)));
//		Log.d(TAG, "x2: " + x2 + "  y2: " + y2);
		float x3 =
				(float) ((width - width * Math.cos(degree2Radian(18))) / 2) + (float) count * width;
		//先算五角星的边长，a=mwidth*cos18/
		float y3 =
				(float) (width * Math.cos(degree2Radian(18)) * Math.cos(degree2Radian(18)) / (2 * (1 + Math.sin(degree2Radian(18)))));
//		Log.d(TAG, "x3: " + x3 + "  y3: " + y3);
		float x4 = (float) ((width * Math.cos(degree2Radian(18)) + x3));
		float y4 = y3;
//		Log.d(TAG, "x4: " + x4 + "  y4: " + y4);
		float x5 =
				(float) (radius - width * Math.cos(degree2Radian(18)) * Math.cos(degree2Radian(72))) + (float) count * width;
		float y5 = y2;
//		Log.d(TAG, "x5: " + x5 + "  y5: " + y5);
		mPath.moveTo(x1, y1);
		mPath.lineTo(x2, y2);
		mPath.lineTo(x3, y3);
		mPath.lineTo(x4, y4);
		mPath.lineTo(x5, y5);
		mPath.close();
		canvas.drawPath(mPath, mPaint);
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

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float startWidth = mWidth / mStarNum;
		mSelectNum = (int) (x / startWidth) + 1;
		if (mOnSelectStarListener != null) {
			mOnSelectStarListener.onSelectStar(mSelectNum);
		}
		invalidate();
		return super.onTouchEvent(event);
	}

	public void setSelectNum(int selectNum) {
		mSelectNum = selectNum;
		if (mOnSelectStarListener != null) {
			mOnSelectStarListener.onSelectStar(mSelectNum);
		}
		invalidate();
	}

	/**
	 * 监听选中的星星
	 *
	 * @param selectStarListener
	 */
	public void setOnSelectStarListener(OnSelectStarListener selectStarListener) {
		mOnSelectStarListener = selectStarListener;
	}

	public interface OnSelectStarListener {
		void onSelectStar(int selectStar);
	}
}
