package com.dip.arzifwatch.utils

import android.content.Context
import android.view.WindowManager
import android.widget.PopupWindow
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

object Utils {
    const val QR_KEY = "qrcode"
    const val EDITING = "editing"
    const val DATABASE_NAME = "wallet_db"
    const val WALLET = "wallet"
    const val BASE_URL = "https://btc.nownodes.io/"

    const val BEP20_KEY = "K5F5VT1RBGC5I7PQ8WA5NQ4IN6NU6Y53R7"
    const val NOWNODE_KEY = "02183bac-5e46-4d8e-8191-220ad8f1272e"
    const val ERC20_KEY = "PHU9TKPVHW95JVDAUNIUAVPAF7EGEDS66Q"

    const val BTC = "https://btc.nownodes.io/api/v2/address/"
    const val BEP2 = "https://dex.binance.org/api/v1/account/"
    const val BEP20 = "https://api.bscscan.com/api"
    const val BCH = "https://bch.nownodes.io/api/v2/address/"
    const val DASH = "https://api.blockchair.com/dash/dashboards/address/"
    const val DOGE = "https://dogechain.info/api/v1/address/balance/"
    const val ERC20 = "https://api.etherscan.io/api"
    const val LTC = "https://ltcbook.nownodes.io/api/v2/address/"
    const val TRC20 = "https://apilist.tronscan.org/api/account"
    const val XRP = "https://api.xrpscan.com/api/v1/account/"

}

fun BigDecimal.calculateDollarValue(symbol: String): String {
    var actualValue = BigDecimal(-1)
    when (symbol) {
        "BitTorrent" -> {
            actualValue = this.divide(BigDecimal(1571442702030000000), 2, RoundingMode.FLOOR)
        }
        "trx" -> {
            actualValue = this.divide(BigDecimal(18.2643957143), 2, RoundingMode.FLOOR)
        }
        "APENFT" -> {
            actualValue = this.divide(BigDecimal(2351130.91663), 2, RoundingMode.FLOOR)
        }
        "Tether USD" -> {
            actualValue = this.divide(BigDecimal(1.00007820911), 2, RoundingMode.FLOOR)
        }
    }
    if (actualValue == BigDecimal(-1)) {
        return "N/A"
    }
    val dec = DecimalFormat("#,###.######")
    return "≈" + dec.format(actualValue.toDouble()) + "$"
}

fun PopupWindow.dimBehind() {
    val container = contentView.rootView
    val context = contentView.context
    val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val p = container.layoutParams as WindowManager.LayoutParams
    p.flags = p.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
    p.dimAmount = 0.7f
    wm.updateViewLayout(container, p)
}

