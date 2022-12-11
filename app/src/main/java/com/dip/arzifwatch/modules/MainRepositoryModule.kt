package com.dip.arzifwatch.modules

import com.dip.arzifwatch.api.ApiService
import com.dip.arzifwatch.db.WalletDao
import com.dip.arzifwatch.repositories.DatabaseRepository
import com.dip.arzifwatch.repositories.NetworkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainRepositoryModule {

    @Provides
    @Singleton
    fun provideNetworkRepository(apiService: ApiService): NetworkRepository {
        return NetworkRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideDatabaseRepository(walletDao: WalletDao): DatabaseRepository {
        return DatabaseRepository(walletDao)
    }
}
