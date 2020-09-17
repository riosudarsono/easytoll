package com.arman.easytoll.utils

import android.content.Context
import com.arman.easytoll.R
import java.text.NumberFormat
import java.util.*

object StringUtils {
    fun toRp(context: Context, price: String?): String? {
        return if (!price.isNullOrEmpty()){
            val rupiahFormat = NumberFormat.getInstance(Locale.CANADA)
            (context.getString(R.string.rupiah, rupiahFormat.format(price.toDouble()).replace(",", ".")))
        } else ""
    }
}