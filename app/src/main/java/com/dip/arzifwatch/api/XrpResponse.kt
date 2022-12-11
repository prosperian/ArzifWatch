package com.dip.arzifwatch.api

import com.google.gson.annotations.SerializedName

data class XrpResponse(
    @SerializedName("sequence") var sequence: Int? = null,
    @SerializedName("xrpBalance") var xrpBalance: String? = null,
    @SerializedName("ownerCount") var ownerCount: Int? = null,
    @SerializedName("Balance") var balance: String,
    @SerializedName("Flags") var flags: Int? = null,
    @SerializedName("LedgerEntryType") var ledgerEntryType: String? = null,
    @SerializedName("PreviousTxnID") var previousTxnID: String? = null,
    @SerializedName("PreviousTxnLgrSeq") var previousTxnLgrSeq: Int? = null,
    @SerializedName("index") var index: String? = null,
    @SerializedName("account") var account: String? = null,
    @SerializedName("parent") var parent: String? = null,
    @SerializedName("initial_balance") var initialBalance: Double? = null,
    @SerializedName("inception") var inception: String? = null,
    @SerializedName("ledger_index") var ledgerIndex: Int? = null,
    @SerializedName("tx_hash") var txHash: String? = null,
)