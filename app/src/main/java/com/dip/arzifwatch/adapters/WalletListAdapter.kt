package com.dip.arzifwatch.adapters

import android.content.Context
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dip.arzifwatch.R
import com.dip.arzifwatch.databinding.PopupLayoutBinding
import com.dip.arzifwatch.databinding.WalletListItemBinding
import com.dip.arzifwatch.interfaces.WalletItemClicked
import com.dip.arzifwatch.models.Wallet
import java.text.NumberFormat
import java.util.*


class WalletListAdapter(private val itemClicked: WalletItemClicked) :
    RecyclerView.Adapter<WalletListAdapter.WalletViewHolder>() {

    private lateinit var context: Context
    private val mList = mutableListOf<Wallet>()

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
            var balance = "N/A"
            try {
                balance =
                    NumberFormat.getNumberInstance(Locale.US)
                        .format(wallet.balance.toBigDecimal()) + "$"
            } catch (e: NumberFormatException) {
            }
            binding.tvListBalance.text = balance

            binding.tvListNetwork.text = wallet.net
            if (wallet.coins.isNotEmpty()) {
                binding.rvListCoins.layoutManager = GridLayoutManager(
                    context, 2, GridLayoutManager.VERTICAL, false
                )
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

        binding.btnPopupEdit.setOnClickListener {
            popUp.dismiss()
            itemClicked.onWalletItemEdit(wallet)
        }

        binding.btnPopupDelete.setOnClickListener {
            popUp.dismiss()
            mList.remove(wallet)
            notifyItemRemoved(position)
            itemClicked.onWalletItemDelete(wallet, mList.size)
        }

    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class WalletViewHolder(val binding: WalletListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}