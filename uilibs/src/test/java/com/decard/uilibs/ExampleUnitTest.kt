package com.decard.uilibs

import junit.framework.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
//        assertEquals(4, 2 + 2)
        var cos = Math.cos(degree2Radian(18))
        assertEquals(4, cos)
    }

    private fun degree2Radian(degree: Int): Double {
        return (Math.PI * degree / 180).toDouble()
    }
}