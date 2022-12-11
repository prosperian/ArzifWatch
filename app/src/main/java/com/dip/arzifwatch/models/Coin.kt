package com.dip.arzifwatch.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Coin(
    var name: String,
    var balance: String,
    var flagUrl: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    ) : Parcelable
