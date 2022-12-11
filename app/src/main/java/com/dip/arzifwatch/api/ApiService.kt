package com.dip.arzifwatch.api

import com.dip.arzifwatch.utils.Utils
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {


    @GET
    suspend fun getBTC(
        @Url url: String,
        @Header("api-key") apiKey: String = Utils.NOWNODE_KEY,
    ): Response<NownodesResponse>

    @GET
    suspend fun getBEP2(
        @Url url: String,
    ): Response<Bep2Response>

    @GET
    suspend fun getBEP20(
        @Url url: String = Utils.BEP20,
        @Query("module") module: String = "account",
        @Query("action") action: String = "balance",
        @Query("api-key") apiKey: String = Utils.BEP20_KEY,
        @Query("address") walletAddress: String
    ): Response<CoinScanResponse>

    @GET
    suspend fun getBCH(
        @Url url: String,
        @Header("api-key") apiKey: String = Utils.NOWNODE_KEY,
    ): Response<NownodesResponse>

//    @GET
//    suspend fun getDASH(
//        @Url url: String = Utils.DASH,
//    ):Response<>

    @GET
    suspend fun getDOGE(
        @Url url: String,
    ): Response<DogeResponse>

    @GET
    suspend fun getERC20(
        @Url url: String = Utils.ERC20,
        @Query("module") module: String = "account",
        @Query("action") action: String = "balance",
        @Query("tag") tag: String = "latest",
        @Query("api-key") apiKey: String = Utils.ERC20_KEY,
        @Query("address") walletAddress: String
    ): Response<CoinScanResponse>

    @GET
    suspend fun getLTC(
        @Url url: String,
        @Header("api-key") apiKey: String = Utils.NOWNODE_KEY,
    ): Response<NownodesResponse>

    @GET
    suspend fun getTRC20(
        @Url url: String = Utils.TRC20,
        @Query("address") walletAddress: String
    ): Response<Trc20Response>

    @GET
    suspend fun getXRP(
        @Url url: String,
    ): Response<XrpResponse>

}