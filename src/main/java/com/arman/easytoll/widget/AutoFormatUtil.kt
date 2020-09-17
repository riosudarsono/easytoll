package com.arman.easytoll.widget

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

/**
 * Created by aldo on 21/08/16.
 */
object AutoFormatUtil {
    private const val FORMAT_NO_DECIMAL = "###,###"
    private const val FORMAT_WITH_DECIMAL = "###,###.###"
    private fun getCharOccurrence(input: String, c: Char): Int {
        var occurrence = 0
        val chars = input.toCharArray()
        for (thisChar in chars) {
            if (thisChar == c) {
                occurrence++
            }
        }
        return occurrence
    }

    @JvmStatic
    fun getCommaOccurrence(input: String): Int {
        return getCharOccurrence(input, ',')
    }

    @JvmStatic
    fun extractDigits(input: String): String {
        return input.replace("\\D+".toRegex(), "")
    }

    private fun formatToStringWithoutDecimal(value: Double): String {
        val formatter: NumberFormat = DecimalFormat(FORMAT_NO_DECIMAL)
        return formatter.format(value)
    }

    @JvmStatic
    fun formatToStringWithoutDecimal(value: String): String {
        return formatToStringWithoutDecimal(value.toDouble())
    }

    fun formatWithDecimal(price: String): String {
        return formatWithDecimal(price.toDouble())
    }

    private fun formatWithDecimal(price: Double): String {
        return NumberFormat.getNumberInstance(Locale.CANADA).format(price)
    }
}