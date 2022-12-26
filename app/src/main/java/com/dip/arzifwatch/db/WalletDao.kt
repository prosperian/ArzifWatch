package com.dip.arzifwatch.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dip.arzifwatch.models.Wallet

@Dao
interface WalletDao {

    @Query("select * from wallet")
    fun getWallets(): MutableList<Wallet>

    @Query("delete from wallet where address=:address")
    fun deleteWallet(address: String)

    @Insert
    fun insertWallet(wallet: Wallet)

    @Update
    fun updateWallet(wallet: Wallet)

}