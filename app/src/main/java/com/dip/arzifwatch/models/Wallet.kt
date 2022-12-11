package com.dip.arzifwatch.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Wallet(
    var address: String,
    var balance: String,
    var net: String? = "",
    var coins: MutableList<Coin> = mutableListOf()
) : Parcelable