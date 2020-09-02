package com.decard.androidtest.net

data class BaseResponse<T>(val code: String, val msg: String, val result: T)