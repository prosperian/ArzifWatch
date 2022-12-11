package com.dip.arzifwatch.api

import com.google.gson.annotations.SerializedName

data class CoinScanResponse(
    @SerializedName("status") var status: String? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("result") var result: String
)
