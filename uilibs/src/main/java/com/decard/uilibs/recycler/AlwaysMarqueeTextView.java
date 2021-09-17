package com.decard.uilibs.recycler;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

/**
 * @author ZJ
 * create at 2021/6/23 16:50
 *   <com.decard.uilibs.recycler.AlwaysMarqueeTextView
 *             android:layout_width="match_parent"
 *             android:layout_height="wrap_content"
 *             android:ellipsize="marquee"
 *             android:focusableInTouchMode="true"
 *             android:marqueeRepeatLimit="marquee_forever"
 *             android:singleLine="true"
 *             android:text="君不见，黄河之水天上来，奔流到海不复回，君不见，高堂明镜悲白发，朝如青丝暮成雪"
 *             android:textColor="#F30F0F"
 *             android:textSize="@dimen/sp_20"
 *             tools:text="君不见，黄河之水天上来，奔流到海不复回，君不见，高堂明镜悲白发，朝如青丝暮成雪" />
 *
 *
 *              android:ellipsize="marquee" //设置为跑马灯效果
 *                 android:marqueeRepeatLimit="marquee_forever" //设置为重复走动
 *                 android:focusable="true" //获取焦点
 *                 android:singleLine="true" //单行文字
 *                 android:focusableInTouchMode="true" //获取focus
 */
public class AlwaysMarqueeTextView extends androidx.appcompat.widget.AppCompatTextView {

	private String empty_char = "                                                     ";
	public AlwaysMarqueeTextView(@NonNull @NotNull Context context) {
		super(context);
	}

	public AlwaysMarqueeTextView(@NonNull @NotNull Context context,
	                             @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public AlwaysMarqueeTextView(@NonNull @NotNull Context context,
	                             @Nullable @org.jetbrains.annotations.Nullable AttributeSet attrs,
	                             int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean isFocused() {
		return true;
	}

	@Override
	public void setText(CharSequence text, BufferType type) {

		super.setText(text, type);
	}
}
