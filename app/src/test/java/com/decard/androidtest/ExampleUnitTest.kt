package com.decard.androidtest

import com.example.commonlibs.utils.BigDecimalUtils
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

        val yuanToFen = BigDecimalUtils.yuanToFen(12345678.08)
        print(yuanToFen)
    }

    @Test
    fun yuan(){
        val yuanToFen = BigDecimalUtils.yuanToFen(10000.00)
        print(yuanToFen)
        Assert.assertEquals(1000000,BigDecimalUtils.yuanToFen(10000.00))
    }

    fun print(msg: String) {
        println(msg)
    }


}