package com.dip.arzifwatch.models

import com.google.gson.annotations.SerializedName

data class Contract(
    @SerializedName("ERC20")
    val erc20: MutableList<ContractCoin>,
    @SerializedName("BEP20")
    val bep20: MutableList<ContractCoin>,
    @SerializedName("TRC20")
    val trc20: MutableList<ContractCoin>,
    @SerializedName("BEP2")
    val bep2: MutableList<ContractCoin>
)

data class ContractCoin(
    val name: String,
    val address: String?,
    val assetName: String?,
    val decimal: Int
)
