package com.arman.easytoll.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class IdNameData(
    var id: String,
    var name: String) : Parcelable {

    override fun toString(): String {
        return name
    }
}