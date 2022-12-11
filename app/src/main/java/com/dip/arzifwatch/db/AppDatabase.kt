package com.dip.arzifwatch.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dip.arzifwatch.models.Wallet

@Database(entities = [Wallet::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun walletDao(): WalletDao
}