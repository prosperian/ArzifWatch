package com.dip.arzifwatch.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dip.arzifwatch.R
import com.dip.arzifwatch.databinding.CoinListItemBinding
import com.dip.arzifwatch.models.Coin
import com.dip.arzifwatch.models.Contract
import com.dip.arzifwatch.models.ContractCoin
import com.dip.arzifwatch.utils.calculateDollarValue
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.math.pow

class CoinListAdapter(
    private val mList: MutableList<Coin>,
    private val contracts: MutableList<ContractCoin>
) :
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
            try {
                val dec = DecimalFormat("#,###.######")
                binding.tvCoinBalance.text = dec.format(coin.balance.toBigDecimal())
                binding.tvCoinDollar.text =
                    coin.balance.toBigDecimal().calculateDollarValue(coin.name)
            } catch (e: NumberFormatException) {
            }


            Glide.with(binding.ivCoinLogo).load(coin.flagUrl).placeholder(R.drawable.coin)
                .into(binding.ivCoinLogo)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class CoinListViewHolder(val binding: CoinListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

}