package com.dip.arzifwatch.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dip.arzifwatch.models.Wallet

@Database(entities = [Wallet::class], version = 4)
@TypeConverters(CoinTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun walletDao(): WalletDao
}