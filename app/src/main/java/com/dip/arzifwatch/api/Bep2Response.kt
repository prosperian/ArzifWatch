package com.dip.arzifwatch.api

import com.google.gson.annotations.SerializedName

data class Bep2Response(
    @SerializedName("account_number") var accountNumber: Int? = null,
    @SerializedName("address") var address: String? = null,
    @SerializedName("balances") var balances: ArrayList<Bep2Balance> = arrayListOf(),
    @SerializedName("flags") var flags: Int? = null,
    @SerializedName("public_key") var publicKey: ArrayList<Int> = arrayListOf(),
    @SerializedName("sequence") var sequence: Int? = null
)

data class Bep2Balance(

    @SerializedName("free") var free: String,
    @SerializedName("frozen") var frozen: String? = null,
    @SerializedName("locked") var locked: String? = null,
    @SerializedName("symbol") var symbol: String

)