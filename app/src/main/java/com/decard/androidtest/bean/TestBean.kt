package com.decard.androidtest.bean

data class TestBean(
    val `out`: List<String>,
    val result: String
){
    override fun toString(): String {
        return "TestBean(`out`=$`out`, result='$result')"
    }
}