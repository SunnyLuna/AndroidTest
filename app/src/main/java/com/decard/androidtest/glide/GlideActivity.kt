package com.decard.androidtest.glide

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.decard.androidtest.R
import kotlinx.android.synthetic.main.activity_glide.*
import org.slf4j.LoggerFactory
import java.io.File

class GlideActivity : AppCompatActivity() {

    private val logger = LoggerFactory.getLogger("---GlideActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glide)

        val url =
            "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fclubimg.club.vmall.com%2Fdata%2Fattachment%2Fforum%2F202108%2F05%2F141257yjxlhxruhag4ercu.jpg&refer=http%3A%2F%2Fclubimg.club.vmall.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1642155387&t=06715331f12ac4a8fce53bda000d74bf"
        val file = File(Environment.getExternalStorageDirectory().path + "/0开机启动页.png")
        if (file.exists()) {
            logger.debug("onCreate: 存在")
        } else {
            logger.debug("onCreate: 不存在")
        }
        Glide.with(this)   //返回RequestManager对象
            .asBitmap()//指定加载的类型为图片，gif默认显示第一帧
            .load(url)  //返回RequestBuilder对象
            .placeholder(R.drawable.hei)//占位符
            .error(R.drawable.qin3)//异常占位符
            .diskCacheStrategy(DiskCacheStrategy.NONE)//硬盘缓存策略
            .into(iv_yan)


        Glide.with(this)   //返回RequestManager对象
            .load(url)  //返回RequestBuilder对象
            .into(simpleTarget)


        Glide.with(this)
            .load(url)
            //默认预加载原始图片大小，而into()方法则默认会根据ImageView控件的大小来动态决定加载图片的大小因此，
            // 如果不将diskCacheStrategy的缓存策略指定成DiskCacheStrategy.SOURCE的话，
            // 很容易会造成我们在预加载完成之后再使用into()方法加载图片，却仍然还是要从网络上去请求图片这种现象。
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)//
            .preload()

        Glide.with(this)
            .load(url)
            .downloadOnly(0,0)
    }

    var simpleTarget: CustomTarget<Drawable> = object : CustomTarget<Drawable>() {
        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {

        }

        override fun onLoadCleared(placeholder: Drawable?) {

        }

    }

}