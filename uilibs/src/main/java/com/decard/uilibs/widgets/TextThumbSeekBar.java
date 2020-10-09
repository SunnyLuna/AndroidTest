package com.decard.uilibs.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSeekBar;

/**
 * 自定义滑块文本显示
 *
 * @author ZJ
 * created at 2020/7/31 15:56
 */
public class TextThumbSeekBar extends AppCompatSeekBar {


    private int mThumbSize;//绘制滑块宽度
    private TextPaint mTextPaint;//绘制文本的大小
    private int mSeekBarMin = 0;//滑块开始值

    public TextThumbSeekBar(Context context) {
        this(context, null, android.R.attr.seekBarStyle);
    }

    public TextThumbSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.seekBarStyle);
    }

    public TextThumbSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mThumbSize = 25;
        mTextPaint = new TextPaint();
        mTextPaint.setColor(Color.parseColor("#2B3034"));
        mTextPaint.setTextSize(22);
        mTextPaint.setTypeface(Typeface.DEFAULT);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setAntiAlias(true);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        int unsignedMin = mSeekBarMin < 0 ? mSeekBarMin * -1 : mSeekBarMin;
//        String progressText = String.valueOf(getProgress() + unsignedMin) + "%";
        String progressText = String.valueOf(getProgress());
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(progressText, 0, progressText.length(), bounds);

        int leftPadding = getPaddingLeft();
        int rightPadding = getPaddingRight();
        int width = getWidth() - leftPadding - rightPadding;
        float progressRatio = (float) getProgress() / getMax();
        float thumbOffset = mThumbSize * (.5f - progressRatio);
        float thumbX = progressRatio * width + leftPadding;
        float thumbY = getHeight() / 2f + bounds.height() / 2f;
        canvas.drawText(progressText, thumbX, thumbY - 40, mTextPaint);
    }

    public void setMix(int min) {
        mSeekBarMin = min;
    }
}
