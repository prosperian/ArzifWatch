package com.dip.arzifwatch.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    fun getBtc(address: String): LiveData<Resource<NownodesResponse>> {
        val wallet = MutableLiveData<Resource<NownodesResponse>>()
        viewModelScope.launch(Dispatchers.IO) {
            wallet.postValue(Resource.Loading())
            try {
                val response = networkRepository.getBtc(address)
                wallet.postValue(ResponseHandler.handleResponse(response))
            } catch (e: Exception) {
                wallet.postValue(ResponseHandler.handleError())
            }
        }
        return wallet
    }

    fun getBep2(address: String): LiveData<Resource<Bep2Response>> {
        val wallet = MutableLiveData<Resource<Bep2Response>>()
        viewModelScope.launch(Dispatchers.IO) {
            wallet.postValue(Resource.Loading())
            try {
                val response = networkRepository.getBEP2(address)
                wallet.postValue(ResponseHandler.handleResponse(response))
            } catch (e: Exception) {
                wallet.postValue(ResponseHandler.handleError())
            }
        }
        return wallet
    }

    fun getBep20(address: String): LiveData<Resource<CoinScanResponse>> {
        val wallet = MutableLiveData<Resource<CoinScanResponse>>()
        viewModelScope.launch(Dispatchers.IO) {
            wallet.postValue(Resource.Loading())
            try {
                val response = networkRepository.getBEP20(address)
                wallet.postValue(ResponseHandler.handleResponse(response))
            } catch (e: Exception) {
                wallet.postValue(ResponseHandler.handleError())
            }
        }
        return wallet
    }

    fun getBch(address: String): LiveData<Resource<NownodesResponse>> {
        val wallet = MutableLiveData<Resource<NownodesResponse>>()
        viewModelScope.launch(Dispatchers.IO) {
            wallet.postValue(Resource.Loading())
            try {
                val response = networkRepository.getBCH(address)
                wallet.postValue(ResponseHandler.handleResponse(response))
            } catch (e: Exception) {
                wallet.postValue(ResponseHandler.handleError())
            }
        }
        return wallet
    }

    fun getDoge(address: String): LiveData<Resource<DogeResponse>> {
        val wallet = MutableLiveData<Resource<DogeResponse>>()
        viewModelScope.launch(Dispatchers.IO) {
            wallet.postValue(Resource.Loading())
            try {
                val response = networkRepository.getDOGE(address)
                wallet.postValue(ResponseHandler.handleResponse(response))
            } catch (e: Exception) {
                wallet.postValue(ResponseHandler.handleError())
            }
        }
        return wallet
    }

    fun getErc20(address: String): LiveData<Resource<CoinScanResponse>> {
        val wallet = MutableLiveData<Resource<CoinScanResponse>>()
        viewModelScope.launch(Dispatchers.IO) {
            wallet.postValue(Resource.Loading())
            try {
                val response = networkRepository.getERC20(address)
                wallet.postValue(ResponseHandler.handleResponse(response))
            } catch (e: Exception) {
                wallet.postValue(ResponseHandler.handleError())
            }
        }
        return wallet
    }

    fun getLtc(address: String): LiveData<Resource<NownodesResponse>> {
        val wallet = MutableLiveData<Resource<NownodesResponse>>()
        viewModelScope.launch(Dispatchers.IO) {
            wallet.postValue(Resource.Loading())
            try {
                val response = networkRepository.getLTC(address)
                wallet.postValue(ResponseHandler.handleResponse(response))
            } catch (e: Exception) {
                wallet.postValue(ResponseHandler.handleError())
            }
        }
        return wallet
    }

    fun getTrc20(address: String): LiveData<Resource<Trc20Response>> {
        val wallet = MutableLiveData<Resource<Trc20Response>>()
        viewModelScope.launch(Dispatchers.IO) {
            wallet.postValue(Resource.Loading())
            try {
                val response = networkRepository.getTRC20(address)
                wallet.postValue(ResponseHandler.handleResponse(response))
            } catch (e: Exception) {
                wallet.postValue(ResponseHandler.handleError())
            }
        }
        return wallet
    }

    fun getXrp(address: String): LiveData<Resource<XrpResponse>> {
        val wallet = MutableLiveData<Resource<XrpResponse>>()
        viewModelScope.launch(Dispatchers.IO) {
            wallet.postValue(Resource.Loading())
            try {
                val response = networkRepository.getXRP(address)
                wallet.postValue(ResponseHandler.handleResponse(response))
            } catch (e: Exception) {
                wallet.postValue(ResponseHandler.handleError())
            }
        }
        return wallet
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