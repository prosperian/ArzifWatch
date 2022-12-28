package com.dip.arzifwatch.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dip.arzifwatch.R
import com.dip.arzifwatch.adapters.WalletListAdapter
import com.dip.arzifwatch.api.Resource
import com.dip.arzifwatch.databinding.FragmentWalletListBinding
import com.dip.arzifwatch.interfaces.WalletItemClicked
import com.dip.arzifwatch.models.Coin
import com.dip.arzifwatch.models.Wallet
import com.dip.arzifwatch.utils.Utils
import com.dip.arzifwatch.viewmodels.AddViewModel
import java.math.BigDecimal

class WalletListFragment : Fragment(R.layout.fragment_wallet_list), WalletItemClicked {

    private lateinit var binding: FragmentWalletListBinding
    private lateinit var adapter: WalletListAdapter
    private lateinit var viewModel: AddViewModel

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

        binding.rvWalletList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = WalletListAdapter(this)
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
                    getInfo(wallet.address, it)
                }
            }
            Toast.makeText(requireContext().applicationContext, "Updated", Toast.LENGTH_LONG).show()
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

    private fun getInfo(address: String, net: Int) {
        when (net) {
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

    private fun getBep2(address: String) {
        viewModel.getBep2(address)
        viewModel.bep2.observe(viewLifecycleOwner) {
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
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    showOnError()

                }
                else -> {}
            }
        }
    }

    private fun getBep20(address: String) {
        viewModel.getBep20(address)
        viewModel.bep20.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Log.d("danial", "success")
                    it.data?.let { data ->
                        addToUpdateList(Wallet(address, data.result))
                    }
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    showOnError()

                }
                else -> {}
            }
        }
    }

    private fun getBtc(address: String) {
        viewModel.getBtc(address)
        viewModel.btc.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Log.d("danial", "success")
                    it.data?.let { data ->
                        addToUpdateList(Wallet(data.address, data.balance))
                    }
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    showOnError()

                }
                else -> {}
            }
        }
    }

    private fun getBch(address: String) {
        viewModel.getBch(address)
        viewModel.bch.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Log.d("danial", "success")
                    it.data?.let { data ->
                        addToUpdateList(Wallet(data.address, data.balance))
                    }
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    showOnError()

                }
                else -> {}
            }
        }
    }

    private fun getDoge(address: String) {
        viewModel.getDoge(address)
        viewModel.doge.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Log.d("danial", "success")
                    it.data?.let { data ->
                        addToUpdateList(Wallet(address, data.balance))
                    }
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    showOnError()

                }
                else -> {}
            }
        }
    }

    private fun getErc20(address: String) {
        viewModel.getErc20(address)
        viewModel.erc20.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Log.d("danial", "success")
                    it.data?.let { data ->
                        addToUpdateList(Wallet(address, data.result))
                    }
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    showOnError()

                }
                else -> {}
            }
        }
    }

    private fun getLtc(address: String) {
        viewModel.getLtc(address)
        viewModel.ltc.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Log.d("danial", "success")
                    it.data?.let { data ->
                        addToUpdateList(Wallet(data.address, data.balance))
                    }
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    showOnError()

                }
                else -> {}
            }
        }
    }

    private fun getTrc20(address: String) {
        viewModel.getTrc20(address)
        viewModel.trc20.observe(viewLifecycleOwner) {
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
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    showOnError()
                }
                else -> {}
            }
        }
    }

    private fun getXrp(address: String) {
        viewModel.getXrp(address)
        viewModel.xrp.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    Log.d("danial", "success")
                    it.data?.let { data ->
                        addToUpdateList(Wallet(address, data.balance))
                    }
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    showOnError()
                }
                else -> {}
            }
        }
    }

    private fun showOnError() {
        Toast.makeText(
            requireContext().applicationContext,
            "Not a valid address",
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