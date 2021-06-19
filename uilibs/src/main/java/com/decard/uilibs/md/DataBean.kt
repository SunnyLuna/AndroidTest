package com.decard.uilibs.md

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DataBean(var img: String, var title: String, var id: String) : Parcelable {

    override fun toString(): String {
        return "DataBean{" +
                "img='" + img + '\'' +
                ", title='" + title + '\'' +
                ", id='" + id + '\'' +
                '}'
    }

}