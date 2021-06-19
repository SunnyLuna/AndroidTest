package com.decard.androidtest.viewmodel

import android.content.Context
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.decard.NDKMethod.BasicOper
import com.decard.NDKMethod.SSCardDriver
import com.decard.androidtest.MyApplication
import com.decard.cardlibs.ParseCardUtils
import com.example.commonlibs.utils.ToastUtils
import java.nio.charset.Charset

class SocialCardViewModel : ViewModel() {
    private val TAG = "---SocialCardViewModel"
    public var socialName: ObservableField<String> = ObservableField()
    public var socialNumber: ObservableField<String> = ObservableField()
    public var socialGender: ObservableField<String> = ObservableField()
    public var socialNative: ObservableField<String> = ObservableField()
    public var socialBorn: ObservableField<String> = ObservableField()
    public var socialEffectiveDate: ObservableField<String> = ObservableField()

    /**
     * 读取社保卡信息
     */
    fun readSocialCard(context: Context) {
        Log.d(
            TAG,
            "readSocialMsg: 开始读社保卡"
        )
        var isReadCard = false
//        val ret = BasicOper.setInitParameter(2, null, 0)
        val rets = BasicOper.setInitParameter(1, "/dev/ttyUSB0", 115200)
        Log.d(TAG, "BasicOper.setInitParameterAuthor:$rets")
        BasicOper.CreateAndroidContext(MyApplication.instance)
        val bytes = ByteArray(1024)
        val ret = SSCardDriver.iReadCardBas(4, bytes)
        Log.d(TAG, "readSocialMsg: $ret")
        do {
            val info = ByteArray(2048)
            val ret =
                SSCardDriver.iReadCardBas(1, info)
            if (ret == 0L) {
                isReadCard = true
                val cardStr = String(info, Charset.forName("GBK"))
                Log.d(TAG, "readSocialCard: $cardStr")
            }
            Log.d(TAG, "readSocialCard: $ret")
        } while (!isReadCard)
    }

    private fun initData(ssCard: String) {
        val idList = ssCard.split("|")
        if (idList.size != 8) {
            ToastUtils.toast(MyApplication.instance, "社保卡数据异常")
            return
        }
        socialName.set(idList[0])
        socialNumber.set(idList[1])
        socialGender.set(ParseCardUtils.getGender(idList[2]))
        socialNative.set(ParseCardUtils.getNation(idList[3]))
        socialBorn.set(
            idList[4].substring(0, 4) + "年" + idList[4].substring(
                4,
                6
            ) + "月" + idList[4].substring(6, 8) + "日"
        )
        if (idList[6] != "" && idList[6].length == 8)
            socialEffectiveDate.set(
                idList[6].substring(0, 4) + "年" + idList[6].substring(
                    4,
                    6
                ) + "月"
            )
    }


}