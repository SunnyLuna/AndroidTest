package com.decard.uilibs.md.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.decard.uilibs.R
import com.decard.uilibs.widgets.EditTextUtils.afterTextChanged
import kotlinx.android.synthetic.main.activity_text_input_layout.*

/**
 * TextInputLayout 主要是作为 EditText 的容器，
 * 从而为 EditText 生成一个浮动的 Label，当用户点击 EditText 的时候，
 * EditText 中的 hint 字符串会自动移到 EditText 的左上角。
 *
 * setCounterEnabled(boolean enabled)	在此布局中是否启用了字符计数器功能。
setCounterMaxLength(int maxLength)	设置要在字符计数器上显示的最大长度。
setBoxBackgroundColorResource(int boxBackgroundColorId)	设置用于填充框的背景色的资源。
setBoxStrokeColor(int boxStrokeColor)	设置轮廓框的笔触颜色。
setCounterOverflowTextAppearance(int counterOverflowTextAppearance)	使用指定的 TextAppearance 资源设置溢出字符计数器的文本颜色和大小。
setCounterOverflowTextColor(ColorStateList counterOverflowTextColor)	使用 ColorStateList 设置溢出字符计数器的文本颜色。(此文本颜色优先于 counterOverflowTextAppearance 中设置的文本颜色)
setCounterTextAppearance(int counterTextAppearance)	使用指定的 TextAppearance 资源设置字符计数器的文本颜色和大小。
setCounterTextColor(ColorStateList counterTextColor)	使用 ColorStateList 设置字符计数器的文本颜色。(此文本颜色优先于 counterTextAppearance 中设置的文本颜色)
setErrorEnabled(boolean enabled)	在此布局中是否启用了错误功能。
setErrorTextAppearance(int errorTextAppearance)	设置来自指定 TextAppearance 资源的错误消息的文本颜色和大小。
setErrorTextColor(ColorStateList errorTextColor)	设置错误消息在所有状态下使用的文本颜色。
setHelperText(CharSequence helperText)	设置将在下方显示的帮助消息 EditText。
setHelperTextColor(ColorStateList helperTextColor)	设置辅助状态在所有状态下使用的文本颜色。
setHelperTextEnabled(boolean enabled)	在此布局中是否启用了辅助文本功能。
setHelperTextTextAppearance(int helperTextTextAppearance)	设置指定 TextAppearance 资源中的辅助文本的文本颜色和大小。
setHint(CharSequence hint)	设置要在浮动标签中显示的提示（如果启用）。
setHintAnimationEnabled(boolean enabled)	是否获取焦点的时候，hint 文本上移到左上角开启动画。
setHintEnabled(boolean enabled)	设置是否在此布局中启用浮动标签功能。
setHintTextAppearance(int resId)	从指定的 TextAppearance 资源设置折叠的提示文本的颜色，大小，样式。
setHintTextColor(ColorStateList hintTextColor)	从指定的 ColorStateList 资源设置折叠的提示文本颜色。
setPasswordVisibilityToggleEnabled(boolean enabled)	启用或禁用密码可见性切换功能。
————————————————
版权声明：本文为CSDN博主「Jaynm」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/jaynm/article/details/106918713
 */
class TextInputLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_input_layout)

        til_username.editText!!.afterTextChanged {
            if (til_username!!.editText!!.text.toString().length > 5) {
                til_username.error = "用户名长度超出限制"
            } else {
                til_username.error = null
            }
        }
    }
}