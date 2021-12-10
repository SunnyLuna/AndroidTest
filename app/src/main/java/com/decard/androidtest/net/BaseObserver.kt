package com.decard.androidtest.net

import android.accounts.NetworkErrorException
import android.util.Log
import com.decard.androidtest.net.bean.BaseResponse
import com.decard.androidtest.net.bean.NetErrorBean
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.slf4j.LoggerFactory
import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * 观察者订阅接口返回的基类
 * @author ZJ
 * created at 2021/7/30 16:48
 */
abstract class BaseObserver<T> : Observer<BaseResponse<T>> {
    private val logger = LoggerFactory.getLogger("---BaseObserver")
    private val TAG = "---BaseObserver"
    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(resultBean: BaseResponse<T>) {
        logger.debug("---BaseObserver  请求成功" + resultBean.resultCode + resultBean.data.toString());
        if (resultBean.resultCode == "0000" && resultBean.success == "true") {
            onSuccess(resultBean.data)
        } else {
            if (resultBean.message != null) {
                onFailure(NetErrorBean(resultBean.resultCode, resultBean.message, false))
            } else {
                onFailure(NetErrorBean(resultBean.resultCode, "请求失败", false))
            }
        }
    }

    override fun onError(e: Throwable) {
        logger.debug("---BaseObserver  请求异常" + e.message)
        if (e is ConnectException
            || e is TimeoutException
            || e is NetworkErrorException
            || e is UnknownHostException
        ) {
            Log.d(TAG, "onError: 网络异常" + e.message)
            onFailure(NetErrorBean("网络异常", e.message.toString(), true))
        } else {
            Log.d(TAG, "onError: 其他异常" + e.message)
            onFailure(NetErrorBean("其他异常", e.message.toString(), false))
        }

    }

    override fun onComplete() {

    }

    /**
     * 返回成功
     *
     * @param t
     * @throws Exception
     */
    @Throws(Exception::class)
    protected abstract fun onSuccess(t: T)

    /**
     * 返回成功了,但是code错误
     *
     * @param t
     * @throws Exception
     */
    @Throws(Exception::class)
    protected fun onCodeError(t: BaseResponse<T>) {
    }

    /**
     * 返回失败
     *
     * @param code
     * @param isNetWorkError 是否是网络错误
     * @throws Exception
     */
    @Throws(Exception::class)
//    protected abstract fun onFailure(code: String, msg: String, isNetWorkError: Boolean)
    protected abstract fun onFailure(netErrorBean: NetErrorBean)


}
