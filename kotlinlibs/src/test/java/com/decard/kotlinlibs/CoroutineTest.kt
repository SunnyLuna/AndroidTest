package com.decard.kotlinlibs

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.Test
import kotlin.concurrent.thread

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CoroutineTest {


    @Test
    fun coroutineTest() {
        GlobalScope.launch {
            delay(1000)
            print("World")
        }
        print("Hello")
        Thread.sleep(2000)
        print("还有谁")
    }

    private fun print(msg: String) {
        println(System.currentTimeMillis().toString() + msg)
    }

    @Test
    fun threadTest() {
        thread {
            Thread.sleep(1000)
            print("World")
        }
        print("Hello")
        Thread.sleep(2000)
        print("还有谁")
    }
}