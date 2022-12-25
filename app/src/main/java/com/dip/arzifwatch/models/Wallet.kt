package com.dip.arzifwatch.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Wallet(
    var address: String,
    var balance: String,
    var net: String? = "",
    var netId: Int? = -1,
    var coins: MutableList<Coin> = mutableListOf(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    ) : Parcelable