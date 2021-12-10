package com.decard.androidtest.net.repository

import androidx.lifecycle.MutableLiveData
import com.decard.androidtest.net.BaseObserver
import com.decard.androidtest.net.WebService
import com.decard.androidtest.net.bean.NetErrorBean
import com.decard.androidtest.net.bean.request.SignInRequest
import com.decard.androidtest.net.bean.response.SignInResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.slf4j.LoggerFactory

/**
 * 获取签到信息
 * @author ZJ
 * create at 2021/7/2 9:06
 */
object SignInRepository {
    private val logger = LoggerFactory.getLogger("---SignInRepository")
    val tokeLiveData = MutableLiveData<ResultNet<Any>>()


    fun getToken(signInRequest: SignInRequest) {

        WebService.createVerifyService().signIn(signInRequest.toMap())
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : BaseObserver<SignInResponse>() {
                override fun onSuccess(t: SignInResponse) {
                    logger.debug("onSuccess: $t")
                    tokeLiveData.postValue(ResultNet.Success(t))
                }

                override fun onFailure(netErrorBean: NetErrorBean) {
                    logger.debug("onFailure: " + netErrorBean)
                    tokeLiveData.postValue(ResultNet.Error(netErrorBean))
                }

            })
    }
}