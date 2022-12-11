package com.dip.arzifwatch.interfaces

import com.dip.arzifwatch.models.Wallet

interface WalletItemClicked {
    fun onWalletItemEdit(wallet: Wallet)
    fun onWalletItemDelete(wallet: Wallet, size: Int)
}