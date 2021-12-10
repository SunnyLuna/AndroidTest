package com.decard.androidtest.net.repository

import com.decard.androidtest.net.bean.NetErrorBean


/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class ResultNet<out T : Any> {

    data class Success<out T : Any>(val data: T) : ResultNet<T>()
    data class Error(val exception: NetErrorBean) : ResultNet<NetErrorBean>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            else -> ""
        }
    }
}