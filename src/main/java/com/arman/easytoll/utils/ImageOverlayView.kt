package com.arman.easytoll.utils

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.arman.easytoll.R

/*
 * Created by Alexander Krol (troy379) on 29.08.16.
 */
class ImageOverlayView : RelativeLayout {
    private var tvDescription: TextView? = null
    private var sharingText: String? = null
    private var mListener: IShareClickListener? = null

    interface IShareClickListener {
        fun onShareClick()
    }

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?)
            : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        init()
    }

    fun setDescription(description: String?) {
        tvDescription!!.text = description
    }

    fun setShareText(text: String?) {
        sharingText = text
    }

    private fun sendShareIntent() {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, sharingText)
        sendIntent.type = "text/plain"
        context.startActivity(sendIntent)
    }

    private fun init() {
        val view = View.inflate(context, R.layout.view_image_overlay, this)
        tvDescription = view.findViewById(R.id.tvDescription)
        view.findViewById<View>(R.id.btnShare)
            .setOnClickListener { mListener!!.onShareClick() }
    }

    fun setListener(listener: IShareClickListener?) {
        try {
            mListener = listener
        } catch (e: ClassCastException) {
            Log.e(
                "ImageOverlayView",
                e.message + " must implement IShareClickListener"
            )
        }
    }
}