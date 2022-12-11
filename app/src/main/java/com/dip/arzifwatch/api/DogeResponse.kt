package com.dip.arzifwatch.api

import com.google.gson.annotations.SerializedName

data class DogeResponse(
    @SerializedName("balance") var balance: String,
    @SerializedName("confirmed") var confirmed: String? = null,
    @SerializedName("unconfirmed") var unconfirmed: String? = null,
    @SerializedName("success") var success: Int? = null
)
