package com.example.commonlibs.utils

/**
 * 初始秘钥类
 */
data class SecretKeyBean(
    var encryptionScheme: String,//加密方案
    var mak0: String,//mak初始秘钥
    var mak1: String,//mak临时秘钥
    var enk0: String,//enk初始秘钥
    var enk1: String//eml临时秘钥
)
