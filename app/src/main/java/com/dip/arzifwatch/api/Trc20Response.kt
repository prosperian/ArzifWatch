package com.dip.arzifwatch.api

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Trc20Response(
    @SerializedName("trc20token_balances") var trc20tokenBalances: ArrayList<Trc20tokenBalances> = arrayListOf(),
    @SerializedName("balance") var balance: BigDecimal?,
    @SerializedName("balances") var balances: MutableList<Trc20Balance> = mutableListOf()
)

data class Trc20Balance(
    @SerializedName("balance") var amount: String,
    @SerializedName("tokenName") var tokenName: String,
    @SerializedName("tokenLogo") var tokenLogo: String,
    )

data class Trc20tokenBalances(

    @SerializedName("tokenId") var tokenId: String? = null,
    @SerializedName("balance") var balance: String,
    @SerializedName("tokenName") var tokenName: String,
    @SerializedName("tokenAbbr") var tokenAbbr: String? = null,
    @SerializedName("tokenDecimal") var tokenDecimal: Int? = null,
    @SerializedName("tokenCanShow") var tokenCanShow: Int? = null,
    @SerializedName("tokenType") var tokenType: String? = null,
    @SerializedName("tokenLogo") var tokenLogo: String,
    @SerializedName("vip") var vip: Boolean? = null,
    @SerializedName("tokenPriceInTrx") var tokenPriceInTrx: Double? = null,
    @SerializedName("amount") var amount: Double? = null,
    @SerializedName("nrOfTokenHolders") var nrOfTokenHolders: Int? = null,
    @SerializedName("transferCount") var transferCount: Int? = null

)
