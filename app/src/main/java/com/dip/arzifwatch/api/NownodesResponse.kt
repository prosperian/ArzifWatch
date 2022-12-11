package com.dip.arzifwatch.api

import com.google.gson.annotations.SerializedName

data class NownodesResponse(
    @SerializedName("page") var page: Int? = null,
    @SerializedName("totalPages") var totalPages: Int? = null,
    @SerializedName("itemsOnPage") var itemsOnPage: Int? = null,
    @SerializedName("address") var address: String,
    @SerializedName("balance") var balance: String,
    @SerializedName("totalReceived") var totalReceived: String? = null,
    @SerializedName("totalSent") var totalSent: String? = null,
    @SerializedName("unconfirmedBalance") var unconfirmedBalance: String? = null,
    @SerializedName("unconfirmedTxs") var unconfirmedTxs: Int? = null,
    @SerializedName("txs") var txs: Int? = null,
    @SerializedName("txids") var txids: MutableList<String> = mutableListOf()
)
