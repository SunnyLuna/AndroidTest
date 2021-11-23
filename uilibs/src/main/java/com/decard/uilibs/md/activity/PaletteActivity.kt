package com.decard.uilibs.md.activity

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import com.decard.uilibs.R

/**
 * 调色板
 *Palette可以提取的颜色如下:
● Vibrant （有活力的）
● Vibrant dark（有活力的 暗色）
● Vibrant light（有活力的 亮色）
● Muted （柔和的）
● Muted dark（柔和的 暗色）
● Muted light（柔和的 亮色）


● getPopulation(): 样本中的像素数量
● getRgb(): 颜色的RBG值
● getHsl(): 颜色的HSL值
● getBodyTextColor(): 主体文字的颜色值
● getTitleTextColor(): 标题文字的颜色值

 */
class PaletteActivity : AppCompatActivity() {
    private val TAG = "---PaletteActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_palette)
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.qin4)
        Palette.from(bitmap).generate {
            val rgb = it!!.vibrantSwatch!!.rgb
            Log.d(TAG, "onCreate: $rgb")
        }
    }
}