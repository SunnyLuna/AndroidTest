package com.decard.kotlinlibs

import kotlinx.coroutines.*
import org.junit.Test
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * 协程基础
 * 本质上，协程是轻量级的线程。 它们在某些 CoroutineScope 上下文中与 launch 协程构建器 一起启动。
 */
class CoroutineBasicTest {


    @Test
    fun coroutineTest() {
        GlobalScope.launch {//在后台启动一个新的协程并继续   创建顶层协程
            delay(1000)// 非阻塞的等待 1 秒钟（默认时间单位是毫秒）
            print("World")
        }
        print("Hello")// 协程已在等待时主线程还在继续
        Thread.sleep(2000) // 阻塞主线程 2 秒钟来保证 JVM 存活
        print("还有谁")
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

    @Test
    fun run() {
        GlobalScope.launch {
            delay(1000L)
            print("World!")
        }
        print("Hello,") // 主线程中的代码会立即执行
        // 调用了 runBlocking 的主线程会一直 阻塞 直到 runBlocking 内部的协程执行完毕。
        runBlocking {     // 但是这个表达式阻塞了主线程
            delay(2000L)  // ……我们延迟 2 秒来保证 JVM 的存活
        }
    }

    //runBlocking  协程构建器将 run1 函数转换为协程
    @Test
    fun run1() = runBlocking {
        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L)
            print("World!")
        }
        print("Hello,") // 主协程在这里会立即执行
        delay(2000L)      // 延迟 2 秒来保证 JVM 存活
    }

    //等待一个作业
    @Test
    fun run2() = runBlocking {
        val launch = GlobalScope.launch {
            delay(1000)
            print("World!")
        }
        print("Hello,")
        launch.join()//等待直到子协程执行结束
    }

    //结构化的并发  我们可以在执行操作所在的指定作用域内启动协程
    @Test
    fun run3() = runBlocking {
        launch {// 在 runBlocking 作用域中启动一个新协程
            delay(1000L)
            print("World!")
        }
        print("Hello,")
    }

    //作用域构建器
    @Test
    fun run4() = runBlocking { // this: CoroutineScope
        launch {
            delay(200L)
            println("Task from runBlocking")
        }

        coroutineScope { // 创建一个协程作用域
            launch {
                delay(500L)
                println("Task from nested launch")
            }

            delay(100L)
            println("Task from coroutine scope") // 这一行会在内嵌 launch 之前输出
        }

        println("Coroutine scope is over") // 这一行在内嵌 launch 执行完毕后才输出
    }

    @Test
    fun RUN5() = runBlocking {
        print("开始测试")
        val string = getString()
        print("最终结果$string")
    }

    suspend fun getString() = suspendCoroutine<String> {
        setTest(object : TestC {
            override fun test(string: String) {
                print("我来了")
                it.resume("你好")
            }
        })
    }

    fun setTest(test: TestC) = runBlocking {
        delay(1000L)
        test.test("Hello")
    }


    interface TestC {
        fun test(string: String)
    }

    private fun print(msg: String) {
        println(System.currentTimeMillis().toString() + ":  " + msg)
    }

}