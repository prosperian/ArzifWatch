package com.dip.arzifwatch.adapters

import android.content.Context
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dip.arzifwatch.R
import com.dip.arzifwatch.databinding.PopupLayoutBinding
import com.dip.arzifwatch.databinding.SurePopupBinding
import com.dip.arzifwatch.databinding.WalletListItemBinding
import com.dip.arzifwatch.interfaces.WalletItemClicked
import com.dip.arzifwatch.models.Wallet
import com.dip.arzifwatch.utils.dimBehind
import java.math.BigDecimal
import java.text.DecimalFormat


class WalletListAdapter(private val itemClicked: WalletItemClicked) :
    RecyclerView.Adapter<WalletListAdapter.WalletViewHolder>() {

    private lateinit var context: Context
    private val mList = mutableListOf<Wallet>()
    private val rvPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        context = parent.context
        val binding =
            WalletListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WalletViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {

        val wallet = mList[position]

        with(holder) {
            binding.ivListMore.setOnClickListener {
                val location = IntArray(2)
                binding.ivListMore.getLocationOnScreen(location)
                val point = Point()
                point.x = location[0]
                point.y = location[1]
                showPopup(context, point, position)
            }

            binding.tvListAddress.text = wallet.address
            try {
                var trueBalance = wallet.balance.toBigDecimal().divide(BigDecimal(1000000))
                if (wallet.net == "ERC20") {
                    trueBalance = wallet.balance.toBigDecimal().divide(BigDecimal(1000000000))
                    trueBalance = trueBalance.divide(BigDecimal(1000000000))
                }
                val dec = DecimalFormat("#,###.######")
                binding.tvListBalance.text =
                    context.resources.getString(R.string.dollar_format, dec.format(trueBalance))
            } catch (e: NumberFormatException) {
            }

            binding.tvListNetwork.text = wallet.net
            if (wallet.coins.isNotEmpty()) {
                binding.rvListCoins.layoutManager = GridLayoutManager(
                    context, 3, GridLayoutManager.HORIZONTAL, false
                )
                binding.rvListCoins.setRecycledViewPool(rvPool)
                binding.rvListCoins.adapter = CoinListAdapter(wallet.coins)
            }
        }

    }

    fun addWallet(wallet: Wallet) {
        mList.add(wallet)
        notifyItemInserted(mList.size)
    }

    fun addWalletList(wallets: MutableList<Wallet>) {
        mList.addAll(wallets)
        notifyDataSetChanged()
    }

    fun updateWallet(wallet: Wallet) {
        var position = -1
        mList.forEachIndexed { i, it ->
            if (wallet.address == it.address) {
                position = i
            }
        }
        if (position != -1) {
            mList.removeAt(position)
            mList.add(position, wallet)
            notifyDataSetChanged()
        }
    }

    private fun showPopup(context: Context, p: Point, position: Int) {
        val wallet = mList[position]

        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val binding = PopupLayoutBinding.inflate(layoutInflater)
        val popUp = PopupWindow(context)
        popUp.contentView = binding.root
        popUp.width = LinearLayout.LayoutParams.WRAP_CONTENT
        popUp.height = LinearLayout.LayoutParams.WRAP_CONTENT
        popUp.isFocusable = true
        val x = 200
        val y = 60
        popUp.setBackgroundDrawable(ColorDrawable())
        popUp.animationStyle = R.style.popup_window_animation
        popUp.showAtLocation(binding.root, Gravity.NO_GRAVITY, p.x + x, p.y + y)
        popUp.dimBehind()

        binding.btnPopupEdit.setOnClickListener {
            popUp.dismiss()
            itemClicked.onWalletItemEdit(wallet)
        }

        binding.btnPopupDelete.setOnClickListener {
            popUp.dismiss()
            surePopup(context, wallet, position)
        }

    }

    private fun surePopup(context: Context, wallet: Wallet, position: Int) {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = SurePopupBinding.inflate(layoutInflater)

        val popup = PopupWindow(context)
        popup.contentView = binding.root
        popup.width = LinearLayout.LayoutParams.WRAP_CONTENT
        popup.height = LinearLayout.LayoutParams.WRAP_CONTENT
        popup.setBackgroundDrawable(null)
        popup.isFocusable = true
        popup.animationStyle = R.style.popup_window_animation
        popup.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
        popup.dimBehind()

        binding.btnPopupYes.setOnClickListener {
            popup.dismiss()
            mList.remove(wallet)
            notifyItemRemoved(position)
            itemClicked.onWalletItemDelete(wallet, mList.size)
        }

        binding.btnPopupNo.setOnClickListener {
            popup.dismiss()
        }

    }


    override fun getItemCount(): Int {
        return mList.size
    }

    inner class WalletViewHolder(val binding: WalletListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}