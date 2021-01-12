package com.decard.uilibs.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 自定义一个滑动球
 *
 * @author ZJ
 * created at 2020/12/15 9:24
 */
public class CustomBall extends View {
	private static final String TAG = "---CustomBall";
	private Paint mPaint;
	private GestureDetector mGestureDetector;
	private float radius;
	private float centerX;
	private float centerY;
	private boolean isTouch;

	public CustomBall(Context context) {
		this(context, null, 0, 0);
	}

	public CustomBall(Context context, @Nullable AttributeSet attrs) {
		this(context, attrs, 0, 0);
	}

	public CustomBall(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
	}

	public CustomBall(Context context, @Nullable AttributeSet attrs, int defStyleAttr,
	                  int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView(context);
	}

	private void initView(Context context) {
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mGestureDetector = new GestureDetector(context, onGestureListener);
		//设置为可点击,监听手势
		setClickable(true);
	}

	float mWidth;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		width = Math.min(width, height);
//		setMeasuredDimension(width, width);
		mWidth = (float) width;
		radius = 50;
		centerX = mWidth / 2;
		centerY = mWidth / 2;
		Log.d(TAG, "onMeasure: width:" + width + "  height:" + height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mPaint.setColor(Color.RED);
		canvas.drawCircle(centerX, centerY, radius, mPaint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		Log.d(TAG,
				"onSizeChanged: w: " + w + "  h: " + h + "   oldw: " + oldw + "   oldh: " + oldh);
		if (w > 0) {
			centerX = (float) w / 2;
		}
		if (h > 0) {
			centerY = (float) h / 2;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float distanceByPoint = getDistanceByPoint(event.getX(), event.getY(), centerX,
					centerY);
			Log.d(TAG, "onTouchEvent: " + distanceByPoint);
			if (getDistanceByPoint(event.getX(), event.getY(), centerX, centerY) < radius) {
				isTouch = true;
			} else {
				isTouch = false;
			}
			Log.d(TAG, "onTouchEvent: " + isTouch);
		}
		return super.onTouchEvent(event);

	}

	private GestureDetector.OnGestureListener onGestureListener =
			new GestureDetector.SimpleOnGestureListener() {
				@Override
				public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
				                        float distanceY) {
					Log.d(TAG, "onScroll: ");
					if (isTouch) {
						centerY -= distanceY;
						centerX -= distanceX;
						Log.d(TAG, "onScroll: centerX:" + centerX + "    centerY:" + centerY);
						//处理边界问题
						if (centerX < radius) {
							centerX = radius;
						} else if (centerX > getWidth() - radius) {
							centerX = getWidth() - radius;
						}
						if (centerY < radius) {
							centerY = radius;
						} else if (centerY > getHeight() - radius) {
							centerY = getHeight() - radius;
						}
						Log.d(TAG, "onScroll: centerX:" + centerX + "    centerY:" + centerY);
						//修改圆心后，通知重绘
						postInvalidate();
					}
					return true;
				}

				@Override
				public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				                       float velocityY) {
					Log.d(TAG, "onFling: velocityX:" + velocityX + "    velocityY:" + velocityY);
					return super.onFling(e1, e2, velocityX, velocityY);
				}
			};


	/**
	 * 计算两点间的距离
	 */
	private float getDistanceByPoint(float x1, float y1, float x2, float y2) {
		double temp = Math.abs((x2 - x1) * (x2 - x1) - (y2 - y1) * (y2 - y1));
		return (float) Math.sqrt(temp);
	}
}
