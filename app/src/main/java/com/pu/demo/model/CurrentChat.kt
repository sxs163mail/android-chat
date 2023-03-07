package com.pu.demo.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CurrentChat(
    var id: String,
    var title: String,
    var ope: Int,
    var iconUrl: String
) : Parcelable