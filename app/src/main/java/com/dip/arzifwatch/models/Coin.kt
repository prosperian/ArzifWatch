package com.dip.arzifwatch.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coin(
    var name: String,
    var balance: String,
    var flagUrl: String
) : Parcelable
