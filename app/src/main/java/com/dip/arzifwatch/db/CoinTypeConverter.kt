package com.dip.arzifwatch.db

import androidx.room.TypeConverter
import com.dip.arzifwatch.models.Coin
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.json.Json


class CoinTypeConverter {
    @TypeConverter
    fun listToJson(value: MutableList<Coin>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Coin>::class.java).toList()
}