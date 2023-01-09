package com.dip.arzifwatch.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dip.arzifwatch.R
import com.dip.arzifwatch.adapters.WalletListAdapter
import com.dip.arzifwatch.api.Resource
import com.dip.arzifwatch.databinding.FragmentWalletListBinding
import com.dip.arzifwatch.interfaces.WalletItemClicked
import com.dip.arzifwatch.models.Coin
import com.dip.arzifwatch.models.Contract
import com.dip.arzifwatch.models.Wallet
import com.dip.arzifwatch.utils.Utils
import com.dip.arzifwatch.viewmodels.AddViewModel
import com.google.gson.Gson
import java.math.BigDecimal
import java.util.*

class WalletListFragment : Fragment(R.layout.fragment_wallet_list), WalletItemClicked {

    private lateinit var binding: FragmentWalletListBinding
    private lateinit var adapter: WalletListAdapter
    private lateinit var viewModel: AddViewModel
    private var updateIndex = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentWalletListBinding.bind(view)
        binding.swipeRefresh.setColorSchemeColors(resources.getColor(R.color.teal_200))

        viewModel = ViewModelProvider(requireActivity())[AddViewModel::class.java]

        binding.btnFab.setOnClickListener {
            AddWalletDialog.newInstance().show(
                childFragmentManager,
                AddWalletDialog.TAG
            )
        }

        val inputStream = resources.openRawResource(R.raw.contracts)
        val scanner = Scanner(inputStream).useDelimiter("\\A")
        val contractJson = if (scanner.hasNext()) scanner.next() else ""
        val contract = Gson().fromJson(contractJson, Contract::class.java)

        binding.rvWalletList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = WalletListAdapter(this, contract)
        binding.rvWalletList.adapter = adapter
        binding.rvWalletList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 10 && binding.btnFab.isShown) {
                    binding.btnFab.hide()
                }

                if (dy < -10 && !binding.btnFab.isShown) {
                    binding.btnFab.show()
                }

                if (!recyclerView.canScrollVertically(-1)) {
                    binding.btnFab.show()
                }
            }
        })

        viewModel.getWallets()
        viewModel.wallets.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isNotEmpty()) {
                    if (!binding.rvWalletList.isVisible) {
                        binding.rvWalletList.visibility = View.VISIBLE
                        binding.ivListWallet.visibility = View.GONE
                        binding.tvListNoWallet.visibility = View.GONE
                    }
                    adapter.addWalletList(it)
                }
            }
        }


        childFragmentManager.setFragmentResultListener(
            Utils.WALLET, viewLifecycleOwner
        ) { requestKey, result ->
            if (requestKey == Utils.WALLET) {
                val wallet = result.getParcelable<Wallet>(Utils.WALLET)
                val editing = result.getBoolean(Utils.EDITING, false)
                wallet?.let {
                    if (!binding.rvWalletList.isVisible) {
                        binding.rvWalletList.visibility = View.VISIBLE
                        binding.ivListWallet.visibility = View.GONE
                        binding.tvListNoWallet.visibility = View.GONE
                    }

                    if (!editing) {
                        adapter.addWallet(wallet)
                        viewModel.wallets.value?.add(wallet)
                        viewModel.addWalletToDb(wallet)
                    } else {
                        addToUpdateList(wallet)
                    }

                }
            }
        }

        binding.swipeRefresh.setOnRefreshListener {
            updateIndex = 0
            binding.swipeRefresh.isRefreshing = false
            if (viewModel.wallets.value?.isEmpty()!!) {
                Toast.makeText(
                    requireContext().applicationContext,
                    "No wallets to update",
                    Toast.LENGTH_LONG
                ).show()
                return@setOnRefreshListener
            }
            viewModel.wallets.value?.forEach { wallet ->
                wallet.netId?.let {
                    getInfo()
                }
            }
        }

    }

    private fun addToUpdateList(wallet: Wallet) {
        viewModel.wallets.value?.forEach {
            if (it.address == wallet.address) {
                wallet.net = it.net
                wallet.netId = it.netId
            }
        }
        viewModel.updateWallet(wallet)
        adapter.updateWallet(wallet)
    }

    private fun getInfo() {
        viewModel.wallets.value?.let {

            if (it.isEmpty()) {
                return
            }
            if (updateIndex > it.size - 1) {
                Toast.makeText(
                    requireContext().applicationContext,
                    "Wallets updated",
                    Toast.LENGTH_LONG
                ).show()
                return
            }
            val wallet = it[updateIndex]
            val address = wallet.address
            when (wallet.netId) {
                0 -> {
                    getBep2(address)
                }
                1 -> {
                    getBep20(address)
                }
                2 -> {
                    getBtc(address)
                }
                3 -> {
                    getBch(address)
                }
                4 -> {
                    getDoge(address)
                }
                5 -> {
                    getErc20(address)
                }
                6 -> {
                    getLtc(address)
                }
                7 -> {
                    getTrc20(address)
                }
                8 -> {
                    getXrp(address)
                }
            }
        }
        updateIndex++

    }

    private fun getBep2(address: String) {
        viewModel.getBep2(address).observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Log.d("danial", "success")
                    it.data?.let { data ->
                        data.address?.let {
                            if (data.balances.isNotEmpty()) {
                                val coins = mutableListOf<Coin>()
                                var sumBalance = BigDecimal(0)
                                data.balances.forEach { bnp ->
                                    sumBalance += bnp.free.toBigDecimal()
                                    coins.add(
                                        Coin(
                                            balance = bnp.free,
                                            name = bnp.symbol,
                                            flagUrl = ""
                                        )
                                    )
                                }
                                addToUpdateList(Wallet(it, sumBalance.toString(), coins = coins))
                            }
                        }
                    }
                    getInfo()
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    if (it.message != null)
                        showOnError(it.message)
                    else
                        showOnError()

                }
                else -> {}
            }
        }
    }

    private fun getBep20(address: String) {
        viewModel.getBep20(address).observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Log.d("danial", "success")
                    it.data?.let { data ->
                        addToUpdateList(Wallet(address, data.result))
                    }
                    getInfo()

                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    if (it.message != null)
                        showOnError(it.message)
                    else
                        showOnError()

                }
                else -> {}
            }
        }
    }

    private fun getBtc(address: String) {
        viewModel.getBtc(address).observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Log.d("danial", "success")
                    it.data?.let { data ->
                        addToUpdateList(Wallet(data.address, data.balance))
                    }
                    getInfo()
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    if (it.message != null)
                        showOnError(it.message)
                    else
                        showOnError()

                }
                else -> {}
            }
        }
    }

    private fun getBch(address: String) {
        viewModel.getBch(address).observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Log.d("danial", "success")
                    it.data?.let { data ->
                        addToUpdateList(Wallet(data.address, data.balance))
                    }
                    getInfo()
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    if (it.message != null)
                        showOnError(it.message)
                    else
                        showOnError()

                }
                else -> {}
            }
        }
    }

    private fun getDoge(address: String) {
        viewModel.getDoge(address).observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Log.d("danial", "success")
                    it.data?.let { data ->
                        addToUpdateList(Wallet(address, data.balance))
                    }
                    getInfo()
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    if (it.message != null)
                        showOnError(it.message)
                    else
                        showOnError()

                }
                else -> {}
            }
        }
    }

    private fun getErc20(address: String) {
        viewModel.getErc20(address).observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Log.d("danial", "success")
                    it.data?.let { data ->
                        addToUpdateList(Wallet(address, data.result))
                    }
                    getInfo()
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    if (it.message != null)
                        showOnError(it.message)
                    else
                        showOnError()

                }
                else -> {}
            }
        }
    }

    private fun getLtc(address: String) {
        viewModel.getLtc(address).observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Log.d("danial", "success")
                    it.data?.let { data ->
                        addToUpdateList(Wallet(data.address, data.balance))
                    }
                    getInfo()
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    if (it.message != null)
                        showOnError(it.message)
                    else
                        showOnError()

                }
                else -> {}
            }
        }
    }

    private fun getTrc20(address: String) {
        viewModel.getTrc20(address).observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Log.d("danial", "success")
                    it.data?.let { data ->
                        data.balance?.let {
                            val coins = mutableListOf<Coin>()
                            data.balances.forEach { mainTrc ->
                                coins.add(
                                    Coin(
                                        name = mainTrc.tokenName,
                                        balance = mainTrc.amount,
                                        flagUrl = mainTrc.tokenLogo
                                    )
                                )
                            }
                            data.trc20tokenBalances.forEach { trc ->
                                coins.add(
                                    Coin(
                                        name = trc.tokenName,
                                        balance = trc.balance,
                                        flagUrl = trc.tokenLogo
                                    )
                                )
                            }
                            addToUpdateList(
                                Wallet(
                                    address = address,
                                    balance = data.balance.toString(),
                                    coins = coins
                                )
                            )
                        }
                    }
                    getInfo()
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    if (it.message != null)
                        showOnError(it.message)
                    else
                        showOnError()
                }
                else -> {}
            }
        }
    }

    private fun getXrp(address: String) {
        viewModel.getXrp(address).observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Log.d("danial", "success")
                    it.data?.let { data ->
                        addToUpdateList(Wallet(address, data.balance))
                    }
                    getInfo()
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    if (it.message != null)
                        showOnError(it.message)
                    else
                        showOnError()
                }
                else -> {}
            }
        }
    }

    private fun showOnError(message: String = "Not a valid address") {
        Toast.makeText(
            requireContext().applicationContext,
            message,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onWalletItemEdit(wallet: Wallet) {
        AddWalletDialog.newInstance(wallet).show(
            childFragmentManager,
            AddWalletDialog.TAG
        )
    }

    override fun onWalletItemDelete(wallet: Wallet, size: Int) {
        if (size == 0) {
            binding.rvWalletList.visibility = View.GONE
            binding.ivListWallet.visibility = View.VISIBLE
            binding.tvListNoWallet.visibility = View.VISIBLE
        }
        var position = -1
        viewModel.wallets.value?.forEachIndexed { i, it ->
            if (wallet.address == it.address) {
                position = i
                return@forEachIndexed
            }
        }
        if (position != -1) {
            viewModel.wallets.value?.removeAt(position)
            viewModel.deleteWallet(wallet)
        }
    }

}