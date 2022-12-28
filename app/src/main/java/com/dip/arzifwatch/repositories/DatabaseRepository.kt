package com.dip.arzifwatch.repositories

import androidx.annotation.WorkerThread
import com.dip.arzifwatch.db.WalletDao
import com.dip.arzifwatch.models.Wallet
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class DatabaseRepository @Inject constructor(private val walletDao: WalletDao) {

    @WorkerThread
    suspend fun deleteWallet(address: String) {
        walletDao.deleteWallet(address)
    }

    @WorkerThread
    suspend fun getWallets(): MutableList<Wallet> {
        return walletDao.getWallets()
    }

    @WorkerThread
    suspend fun insertWallet(wallet: Wallet) {
        walletDao.insertWallet(wallet)
    }

    @WorkerThread
    fun updateWallet(wallet: Wallet) {
        walletDao.deleteWallet(wallet.address)
        walletDao.insertWallet(wallet)
    }

}