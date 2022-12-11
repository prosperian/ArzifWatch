package com.dip.arzifwatch.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dip.arzifwatch.R
import com.dip.arzifwatch.adapters.WalletListAdapter
import com.dip.arzifwatch.databinding.FragmentWalletListBinding
import com.dip.arzifwatch.interfaces.WalletItemClicked
import com.dip.arzifwatch.models.Wallet
import com.dip.arzifwatch.utils.Utils

class WalletListFragment : Fragment(R.layout.fragment_wallet_list), WalletItemClicked {

    private lateinit var binding: FragmentWalletListBinding
    private lateinit var adapter: WalletListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentWalletListBinding.bind(view)

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


        childFragmentManager.setFragmentResultListener(
            Utils.WALLET, viewLifecycleOwner
        ) { requestKey, result ->
            if (requestKey == Utils.WALLET) {
                val wallet = result.getParcelable<Wallet>(Utils.WALLET)
                wallet?.let {
                    if (!binding.rvWalletList.isVisible) {
                        binding.rvWalletList.visibility = View.VISIBLE
                        binding.ivListWallet.visibility = View.GONE
                        binding.tvListNoWallet.visibility = View.GONE
                    }
                    adapter.addWallet(wallet)
                    //TODO: add to database
                }
            }
        }

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
        //TODO: remove from database
    }

}