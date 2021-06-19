package com.decard.androidtest.viewmodel

import android.content.Context
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.decard.NDKMethod.BasicOper
import com.decard.entitys.IDCard

class IdCardViewModel : ViewModel() {

    private val TAG = "---ShowIdViewModel"
    public var idName: ObservableField<String> = ObservableField()
    public var idGender: ObservableField<String> = ObservableField()
    public var idNative: ObservableField<String> = ObservableField()
    public var idYear: ObservableField<String> = ObservableField()
    public var idMonth: ObservableField<String> = ObservableField()
    public var idDay: ObservableField<String> = ObservableField()
    public var idAddress: ObservableField<String> = ObservableField()
    public var idNumber: ObservableField<String> = ObservableField()
    public var idIssuingAuthority: ObservableField<String> = ObservableField()//签发机关
    public var idEffectiveDate: ObservableField<String> = ObservableField()
    public var idPicture: ObservableField<String> = ObservableField()
    fun readIdCard(context: Context) {
        Thread {
            var openResult = -1
            do {
                val deviceModel = Build.MODEL
                openResult = if (deviceModel.contains("Z90N") || deviceModel.contains("z90n")) {
                    Log.d(TAG, "readIdCard: Z90")
                    BasicOper.dc_open(
                        "COM",
                        context,
                        "/dev/ttyHSL1",
                        115200
                    )
                } else if (deviceModel.contains("F11") || deviceModel.contains("f11")) {
                    Log.d(TAG, "readIdCard: F11")
                    BasicOper.dc_open(
                        "COM",
                        context,
                        "/dev/ttyUSB0",
                        115200
                    )
                } else {
                    Log.d(TAG, "readIdCard: USB")
                    BasicOper.dc_open("AUSB", context, "", 0)
                }
                Log.d(TAG, "readIdCard:openResult: $openResult")
            } while (openResult < 0 || openResult == 0)

            var idCardData: IDCard? = BasicOper.dc_SamAReadCardInfo(1)
            while (idCardData == null) {
                SystemClock.sleep(50)
                idCardData = BasicOper.dc_SamAReadCardInfo(1)
            }
            Log.d(TAG, "readIdCard: $idCardData")
            initData(idCardData)
        }.start()
    }

    fun initData(idCard: IDCard) {
        idName.set(idCard.name.trim())
        idGender.set(idCard.sex)
        idNative.set(idCard.nation)
        idYear.set(idCard.birthday.substring(0, 4))
        idMonth.set(idCard.birthday.substring(4, 6))
        idDay.set(idCard.birthday.substring(6, 8))
        idAddress.set(idCard.address)
        idNumber.set(idCard.id)
        idEffectiveDate.set(
            idCard.startTime.substring(0, 4) + "." +
                    idCard.startTime.substring(4, 6) + "." +
                    idCard.startTime.substring(6, 8) + "—" +
                    idCard.endTime.substring(0, 4) + "." +
                    idCard.endTime.substring(4, 6) + "." +
                    idCard.endTime.substring(6, 8)
        )
        idIssuingAuthority.set(idCard.office)
        idPicture.set(idCard.photoDataHexStr)
    }


}