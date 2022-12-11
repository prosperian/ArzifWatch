package com.dip.arzifwatch.repositories

import androidx.annotation.WorkerThread
import com.dip.arzifwatch.api.*
import com.dip.arzifwatch.utils.Utils
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Response
import javax.inject.Inject

@Module
@InstallIn(SingletonComponent::class)
class NetworkRepository @Inject constructor(private val apiService: ApiService) {

    @WorkerThread
    suspend fun getBtc(address: String): Response<NownodesResponse> {
        return apiService.getBTC(url = Utils.BTC + address)
    }

    @WorkerThread
    suspend fun getBEP2(address: String): Response<Bep2Response> {
        return apiService.getBEP2(url = Utils.BEP2 + address)
    }

    @WorkerThread
    suspend fun getBEP20(address: String): Response<CoinScanResponse> {
        return apiService.getBEP20(walletAddress = address)
    }

    @WorkerThread
    suspend fun getBCH(address: String): Response<NownodesResponse> {
        return apiService.getBCH(url = Utils.BCH + address)
    }

    @WorkerThread
    suspend fun getDOGE(address: String): Response<DogeResponse> {
        return apiService.getDOGE(url = Utils.DOGE + address)
    }

    @WorkerThread
    suspend fun getERC20(address: String): Response<CoinScanResponse> {
        return apiService.getERC20(walletAddress = address)
    }

    @WorkerThread
    suspend fun getLTC(address: String): Response<NownodesResponse> {
        return apiService.getLTC(url = Utils.LTC + address)
    }

    @WorkerThread
    suspend fun getTRC20(address: String): Response<Trc20Response> {
        return apiService.getTRC20(walletAddress = address)
    }

    @WorkerThread
    suspend fun getXRP(address: String): Response<XrpResponse> {
        return apiService.getXRP(url = Utils.XRP + address)
    }


}