package com.example.commonlibs.utils

import org.junit.Assert
import org.junit.Test

class BigDecimalTest {

    private val TAG = "---BigDecimalTest"

    @Test
    fun add() {
        println("add: ${BigDecimalUtils.add(0.01, 0.05).toDouble()}")
        Assert.assertEquals(
            0.06,
            BigDecimalUtils.add(0.01, 0.05).toDouble(),0.000000000000000000000000000001
        )
    }
    @Test
    fun subtract() {
        println("subtract: ${BigDecimalUtils.subtract(0.01, 0.05).toDouble()}")
        Assert.assertEquals(
            -0.04,
            BigDecimalUtils.subtract(0.01, 0.05).toDouble(),0.000000000000000000000000000001
        )
    }

    @Test
    fun mul() {
        println("mul: ${BigDecimalUtils.multiply(0.01, 0.05).toDouble()}")
        Assert.assertEquals(
            0.0005,
            BigDecimalUtils.multiply(0.01, 0.05).toDouble(),0.000000000000000000000000000001
        )
    }

    @Test
    fun divide() {
        println("divide: ${BigDecimalUtils.divide(0.01, 0.05).toDouble()}")
        Assert.assertEquals(
            0.2,
            BigDecimalUtils.divide(0.01, 0.05).toDouble(),0.000000000000000000000000000001
        )
    }


    @Test
    fun yuan(){
        Assert.assertEquals(1000000,BigDecimalUtils.yuanToFen(10000.00))
    }
}