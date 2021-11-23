package com.decard.androidtest.utils

import androidx.annotation.IntDef

/**
 *
 * @author ZJ
 * create at 2021/11/1 16:50
 *  @Target 注解目标: 指定该注解用于什么类型的元素(类,函数,属性,表达式等);
 *@Retention 注解保留: 指定该注解是否存在编译后的class文件,以及在运行时能否通过反射可见(默认都是true);
 *@Repeatable 允许该注解在单个元素上多次使用;
 *@MustBeDocumented 指定该注解是公有API的一部分,且应该包含在生成的API文档中(类或方法的文档注释)
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.SOURCE)
annotation class CardType()

const val SUNNY = 1
const val CLOUDY = 2
const val RAIN = 3
const val SNOW = 4




fun setWeather(@CardType weather: Int) {}
