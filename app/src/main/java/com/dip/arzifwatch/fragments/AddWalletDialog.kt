package com.dip.arzifwatch.fragments

import android.content.ClipboardManager
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.dip.arzifwatch.R
import com.dip.arzifwatch.adapters.WalletListAdapter
import com.dip.arzifwatch.api.Resource
import com.dip.arzifwatch.databinding.DialogAddWalletBinding
import com.dip.arzifwatch.models.Coin
import com.dip.arzifwatch.models.Wallet
import com.dip.arzifwatch.utils.Utils
import com.dip.arzifwatch.viewmodels.AddViewModel
import java.math.BigDecimal


class AddWalletDialog : DialogFragment(R.layout.dialog_add_wallet) {

    private lateinit var binding: DialogAddWalletBinding
    private lateinit var viewModel: AddViewModel
    private var wallet: Wallet? = null
    private var selectedNet = 0
    private var isEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            wallet = it.getParcelable(Utils.WALLET)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.attributes?.width = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setGravity(Gravity.BOTTOM)

        binding = DialogAddWalletBinding.bind(view)

        viewModel = ViewModelProvider(requireActivity())[AddViewModel::class.java]

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.spinner_dropdown_view,
            R.id.spinner_text,
            resources.getStringArray(R.array.networks)
        )
        adapter.setDropDownViewResource(R.layout.spinner_item)
        binding.spAddNetworks.adapter = adapter

        binding.ivAddPaste.setOnClickListener {
            val clipboard =
                ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)
            clipboard?.let {
                it.primaryClip?.let { clip ->
                    clip.getItemAt(0)?.let { item ->
                        binding.etAddAddress.setText(item.text.toString())
                    }
                }
            }
        }


        binding.spAddNetworks.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedNet = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        binding.btnAddSave.setOnClickListener {
            binding.etAddAddress.text?.let {
                if (it.toString().isNotEmpty()) {
                    getInfo(address = it.toString(), net = selectedNet)
                }
            }
        }

        wallet?.let {
            isEditing = true
            binding.etAddAddress.setText(it.address)
            val networks = resources.getStringArray(R.array.networks)
            networks.forEachIndexed { index, net ->
                if (net == it.net) {
                    binding.spAddNetworks.setSelection(index)
                }
            }
        }


    }

    private fun addToList(wallet: Wallet) {
        val netList = resources.getStringArray(R.array.networks)
        wallet.net = netList[selectedNet]
        wallet.netId = selectedNet
        parentFragmentManager.setFragmentResult(
            Utils.WALLET,
            Bundle().apply {
                putParcelable(Utils.WALLET, wallet)
                putBoolean(Utils.EDITING, isEditing)
            }
        )
        dismiss()
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
                                addToList(Wallet(it, data.balances[0].free))
                            }
                        }
                    }
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    Toast.makeText(
                        requireContext().applicationContext,
                        "Not a valid address",
                        Toast.LENGTH_LONG
                    ).show()

                }
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
                        addToList(Wallet(address, data.result))
                    }
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    Toast.makeText(
                        requireContext().applicationContext,
                        "Not a valid address",
                        Toast.LENGTH_LONG
                    ).show()

                }
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
                        addToList(Wallet(data.address, data.balance))
                    }
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    Toast.makeText(
                        requireContext().applicationContext,
                        "Not a valid address",
                        Toast.LENGTH_LONG
                    ).show()

                }
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
                        addToList(Wallet(data.address, data.balance))
                    }
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    Toast.makeText(
                        requireContext().applicationContext,
                        "Not a valid address",
                        Toast.LENGTH_LONG
                    ).show()

                }
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
                        addToList(Wallet(address, data.balance))
                    }
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    Toast.makeText(
                        requireContext().applicationContext,
                        "Not a valid address",
                        Toast.LENGTH_LONG
                    ).show()

                }
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
                        addToList(Wallet(address, data.result))
                    }
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    Toast.makeText(
                        requireContext().applicationContext,
                        "Not a valid address",
                        Toast.LENGTH_LONG
                    ).show()

                }
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
                        addToList(Wallet(data.address, data.balance))
                    }
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    Toast.makeText(
                        requireContext().applicationContext,
                        "Not a valid address",
                        Toast.LENGTH_LONG
                    ).show()

                }
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
                            addToList(
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
                    Toast.makeText(
                        requireContext().applicationContext,
                        "Not a valid address",
                        Toast.LENGTH_LONG
                    ).show()

                }
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
                        addToList(Wallet(address, data.balance))
                    }
                }

                is Resource.Loading -> {
                    Log.d("danial", "loading")
                }

                is Resource.Error -> {
                    Toast.makeText(
                        requireContext().applicationContext,
                        "Not a valid address",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    companion object {

        const val TAG = "addWalletDialog"

        @JvmStatic
        fun newInstance(wallet: Wallet? = null) = AddWalletDialog().apply {
            wallet?.let {
                arguments = Bundle().apply {
                    putParcelable(Utils.WALLET, wallet)
                }
            }
        }
    }

}