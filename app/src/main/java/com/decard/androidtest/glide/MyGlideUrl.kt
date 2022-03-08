package com.decard.androidtest.glide

import com.bumptech.glide.load.model.GlideUrl

/**
 *修改图片的连接，移除尾部的参数
 * @author ZJ
 * create at 2021/12/16 19:14
 */
class MyGlideUrl(var url: String) : GlideUrl(url) {

    override fun getCacheKey(): String {
        return super.getCacheKey()

    }

    fun findTokenParam(): String {
        var tokenParam = ""
        val tokenKeyIndex: Int =
            if (url.indexOf("?token=") >= 0) url.indexOf("?token=") else url.indexOf("&token=")
        if (tokenKeyIndex != -1) {
            val nextAndIndex = url.indexOf("&", tokenKeyIndex + 1);
            tokenParam = if (nextAndIndex != -1) {
                url.substring(tokenKeyIndex + 1, nextAndIndex + 1);
            } else {
                url.substring(tokenKeyIndex);
            }
        }
        return tokenParam;
    }
}