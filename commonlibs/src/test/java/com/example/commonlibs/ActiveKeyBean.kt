package com.example.commonlibs

/**
 * 激活终端
 * @author ZJ
 * created at 2021/6/10 13:42
 */
data class ActiveKeyBean(
    var encryptionScheme: String,//加密方案
    var length: String,//报文体长度
    var terminalKeyVersion: String,//终端密钥版本
    var enkSecretKey: String,//加密密钥  ENK0加密ENK1
    var enkCheckValue: String,//加密密钥checkValue
    var makSecretKey: String,//加密密钥   MAK0 加 密 MAK1
    var makCheckValue: String,//加密密钥checkValue
    var reservedDomain: String = "0000000000",//保留域

) {
    public fun getMessage(): String {
        return encryptionScheme + length + terminalKeyVersion +
                enkSecretKey + enkCheckValue + makSecretKey + makCheckValue + reservedDomain
    }
}
