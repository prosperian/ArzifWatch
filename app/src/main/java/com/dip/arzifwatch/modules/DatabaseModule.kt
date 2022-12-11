package com.dip.arzifwatch.modules

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.room.Room
import com.dip.arzifwatch.db.AppDatabase
import com.dip.arzifwatch.db.WalletDao
import com.dip.arzifwatch.models.Wallet
import com.dip.arzifwatch.utils.Utils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule  {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            Utils.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }


    @Provides
    @Singleton
    fun provideWalletDao(appDatabase: AppDatabase): WalletDao {
        return appDatabase.walletDao()
    }

}