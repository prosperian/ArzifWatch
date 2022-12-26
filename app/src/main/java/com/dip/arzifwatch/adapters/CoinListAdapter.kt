package com.dip.arzifwatch.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dip.arzifwatch.databinding.CoinListItemBinding
import com.dip.arzifwatch.models.Coin
import java.math.BigDecimal
import java.text.DecimalFormat
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
                val trueBalance = coin.balance.toBigDecimal().divide(BigDecimal(1000000))
                val dec = DecimalFormat("#,###.######")
                binding.tvCoinBalance.text = dec.format(trueBalance)

//                balance =
//                    NumberFormat.getNumberInstance(Locale.US).format(dec)
            } catch (e: NumberFormatException) {
            }


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