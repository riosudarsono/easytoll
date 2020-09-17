package com.arman.easytoll.widget

import android.content.Context
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.arman.easytoll.R
import com.arman.easytoll.widget.AutoFormatUtil.extractDigits
import com.arman.easytoll.widget.AutoFormatUtil.formatToStringWithoutDecimal
import com.arman.easytoll.widget.AutoFormatUtil.formatWithDecimal
import com.arman.easytoll.widget.AutoFormatUtil.getCommaOccurrence
import java.util.*

/**
 * Created by aldo on 21/08/16.
 */
class AutoFormatEditText : AppCompatEditText {
    private var prevCommaAmount = 0
    private var isFormatting = false
    private var isDecimal = false

    constructor(context: Context?) : super(context!!) {
        initialize()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        initialize(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {
        initialize(attrs)
    }

    private fun initialize(attrs: AttributeSet? = null) {
        setInputFilter(attrs)
        obtainAttributes(attrs)
        setSoftInputKeyboard()
    }

    private fun setInputFilter(attrs: AttributeSet?) {
        var maxLength = MAX_LENGTH
        if (attrs != null) {
            val maxLengthAttrs = intArrayOf(android.R.attr.maxLength)
            val typedArrays = context
                .obtainStyledAttributes(attrs, maxLengthAttrs)
            maxLength = try {
                typedArrays.getInteger(0, MAX_LENGTH)
            } finally {
                typedArrays.recycle()
            }
        }

        // limit maximum length for input number
        val inputFilterArray = arrayOfNulls<InputFilter>(1)
        inputFilterArray[0] = LengthFilter(maxLength)
        filters = inputFilterArray
    }

    private fun obtainAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArrays = context
                .obtainStyledAttributes(attrs, R.styleable.AutoFormatEditText)
            isDecimal = try {
                typedArrays.getBoolean(R.styleable.AutoFormatEditText_isDecimal, false)
            } finally {
                typedArrays.recycle()
            }
        }
    }

    private fun setSoftInputKeyboard() {
        keyListener = DigitsKeyListener(false, isDecimal)
    }

    fun updateSoftInputKeyboard(isDecimal: Boolean) {
        this.isDecimal = isDecimal
        setSoftInputKeyboard()
        invalidate()
        requestLayout()
    }

    override fun onTextChanged(s: CharSequence, start: Int, lengthBefore: Int, lengthAfter: Int) {
        if (isFormatting) {
            return
        }
        if (s.length > 0) {
            isFormatting = true
            formatInput(s.toString(), start, lengthAfter)
        }
        isFormatting = false
    }

    /**
     * This is the main method which makes use of addNum method.
     *
     * @param input  an input text.
     * @param start  start of a text.
     * @param action delete or insert(0 == Delete and 1 == Insert) text.
     * @return Nothing.
     */
    private fun formatInput(input: String, start: Int, action: Int) {
        val sbResult = StringBuilder()
        val result: String
        var newStart = start
        try {
            // Extract value without its comma
            val digitAndDotText = input.replace(",", "")
            var commaAmount = 0

            // if user press . turn it into 0.
            if (input.startsWith(".") && input.length == 1) {
                setText("0.")
                setSelection(text.toString().length)
                return
            }

            // if user press . when number already exist turns it into comma
            if (input.startsWith(".") && input.length > 1) {
                val st = StringTokenizer(input)
                val afterDot = st.nextToken(".")
                setText("0." + extractDigits(afterDot))
                setSelection(2)
                return
            }
            if (digitAndDotText.contains(".")) {
                // escape sequence for .
                val wholeText =
                    digitAndDotText.split("\\.".toRegex()).toTypedArray()
                if (wholeText.size == 0) {
                    return
                }

                // in 150,000.45 non decimal is 150,000 and decimal is 45
                val nonDecimal = wholeText[0]
                if (nonDecimal.length == 0) {
                    return
                }

                // only format the non-decimal value
                result = formatToStringWithoutDecimal(nonDecimal)
                sbResult
                    .append(result)
                    .append(".")
                if (wholeText.size > 1) {
                    sbResult.append(wholeText[1])
                }
            } else {
                result = formatWithDecimal(digitAndDotText)
                sbResult.append(result)
            }
            newStart += if (action == ACTION_DELETE) 0 else 1

            // calculate comma amount in edit text
            commaAmount += getCommaOccurrence(result)

            // flag to mark whether new comma is added / removed
            if (commaAmount >= 1 && prevCommaAmount != commaAmount) {
                newStart += if (action == ACTION_DELETE) -1 else 1
                prevCommaAmount = commaAmount
            }

            // case when deleting without comma
            if (commaAmount == 0 && action == ACTION_DELETE && prevCommaAmount != commaAmount) {
                newStart -= 1
                prevCommaAmount = commaAmount
            }

            // case when deleting without dots
            if (action == ACTION_DELETE && !sbResult.toString()
                    .contains(".")
                && prevCommaAmount != commaAmount
            ) {
                newStart = start
                prevCommaAmount = commaAmount
            }
            val resultLength = sbResult.toString().length

            // case when dots become comma and vice versa.
            if (action == ACTION_INSERT && commaAmount == 0 && sbResult.toString()
                    .contains(".")
            ) {
                newStart = start
            }
            setText(sbResult.toString())

            // ensure newStart is within result length
            if (newStart > resultLength) {
                newStart = resultLength
            } else if (newStart < 0) {
                newStart = 0
            }
            setSelection(newStart)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val MAX_LENGTH = 19
        private const val ACTION_DELETE = 0
        private const val ACTION_INSERT = 1
    }
}