package com.dip.arzifwatch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dip.arzifwatch.databinding.CoinListItemBinding
import com.dip.arzifwatch.models.Coin
import java.text.NumberFormat
import java.util.*

class CoinListAdapter(private val mList: MutableList<Coin>) :
    RecyclerView.Adapter<CoinListAdapter.CoinListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinListViewHolder {
        val binding =
            CoinListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoinListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinListViewHolder, position: Int) {
        val coin = mList[position]
        with(holder) {
            binding.tvCoinName.text = coin.name
            var balance = "N/A"
            try {
                balance =
                    NumberFormat.getNumberInstance(Locale.US).format(coin.balance.toBigDecimal()) + "$"
            } catch (e: NumberFormatException) { }

            binding.tvCoinBalance.text = balance

            Glide.with(binding.ivCoinLogo).load(coin.flagUrl).into(binding.ivCoinLogo)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class CoinListViewHolder(val binding: CoinListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

}