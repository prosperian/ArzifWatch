package com.dip.arzifwatch.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dip.arzifwatch.api.*
import com.dip.arzifwatch.models.Wallet
import com.dip.arzifwatch.repositories.DatabaseRepository
import com.dip.arzifwatch.repositories.NetworkRepository
import com.dip.arzifwatch.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val databaseRepository: DatabaseRepository
) :
    ViewModel() {

    private val _wallets = SingleLiveEvent<MutableList<Wallet>>()
    val wallets get() = _wallets

    private val _btc: SingleLiveEvent<Resource<NownodesResponse>> = SingleLiveEvent()
    val btc get() = _btc

    private val _bep2: SingleLiveEvent<Resource<Bep2Response>> = SingleLiveEvent()
    val bep2 get() = _bep2

    private val _bep20: SingleLiveEvent<Resource<CoinScanResponse>> = SingleLiveEvent()
    val bep20 get() = _bep20

    private val _bch: SingleLiveEvent<Resource<NownodesResponse>> = SingleLiveEvent()
    val bch get() = _bch

    private val _doge: SingleLiveEvent<Resource<DogeResponse>> = SingleLiveEvent()
    val doge get() = _doge

    private val _erc20: SingleLiveEvent<Resource<CoinScanResponse>> = SingleLiveEvent()
    val erc20 get() = _erc20

    private val _ltc: SingleLiveEvent<Resource<NownodesResponse>> = SingleLiveEvent()
    val ltc get() = _ltc

    private val _trc20: SingleLiveEvent<Resource<Trc20Response>> = SingleLiveEvent()
    val trc20 get() = _trc20

    private val _xrp: SingleLiveEvent<Resource<XrpResponse>> = SingleLiveEvent()
    val xrp get() = _xrp

    fun getBtc(address: String) = viewModelScope.launch(Dispatchers.IO) {
        _btc.postValue(Resource.Loading())
        val response = networkRepository.getBCH(address)
        _btc.postValue(ResponseHandler.handleResponse(response))
    }

    fun getBep2(address: String) = viewModelScope.launch(Dispatchers.IO) {
        _bep2.postValue(Resource.Loading())
        val response = networkRepository.getBEP2(address)
        _bep2.postValue(ResponseHandler.handleResponse(response))
    }

    fun getBep20(address: String) = viewModelScope.launch(Dispatchers.IO) {
        _bep20.postValue(Resource.Loading())
        val response = networkRepository.getBEP20(address)
        _bep20.postValue(ResponseHandler.handleResponse(response))
    }

    fun getBch(address: String) = viewModelScope.launch(Dispatchers.IO) {
        _bch.postValue(Resource.Loading())
        val response = networkRepository.getBCH(address)
        _bch.postValue(ResponseHandler.handleResponse(response))
    }

    fun getDoge(address: String) = viewModelScope.launch(Dispatchers.IO) {
        _doge.postValue(Resource.Loading())
        val response = networkRepository.getDOGE(address)
        _doge.postValue(ResponseHandler.handleResponse(response))
    }

    fun getErc20(address: String) = viewModelScope.launch(Dispatchers.IO) {
        _erc20.postValue(Resource.Loading())
        val response = networkRepository.getERC20(address)
        _erc20.postValue(ResponseHandler.handleResponse(response))
    }

    fun getLtc(address: String) = viewModelScope.launch(Dispatchers.IO) {
        _ltc.postValue(Resource.Loading())
        val response = networkRepository.getLTC(address)
        _ltc.postValue(ResponseHandler.handleResponse(response))
    }

    fun getTrc20(address: String) = viewModelScope.launch(Dispatchers.IO) {
        _trc20.postValue(Resource.Loading())
        val response = networkRepository.getTRC20(address)
        _trc20.postValue(ResponseHandler.handleResponse(response))
    }

    fun getXrp(address: String) = viewModelScope.launch(Dispatchers.IO) {
        _xrp.postValue(Resource.Loading())
        val response = networkRepository.getXRP(address)
        _xrp.postValue(ResponseHandler.handleResponse(response))
    }

    fun addWalletToDb(wallet: Wallet) = viewModelScope.launch(Dispatchers.IO) {
        databaseRepository.insertWallet(wallet)
    }

    fun getWallets() = viewModelScope.launch(Dispatchers.IO) {
        _wallets.postValue(databaseRepository.getWallets())
    }

    fun deleteWallet(wallet: Wallet) = viewModelScope.launch(Dispatchers.IO) {
        databaseRepository.deleteWallet(wallet.address)
    }

    fun updateWallet(wallet: Wallet) = viewModelScope.launch(Dispatchers.IO) {
        databaseRepository.updateWallet(wallet)
    }

}