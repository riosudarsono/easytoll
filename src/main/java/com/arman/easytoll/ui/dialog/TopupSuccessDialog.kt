package com.arman.easytoll.ui.dialog


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.arman.easytoll.R
import com.arman.easytoll.utils.StringUtils
import kotlinx.android.synthetic.main.dialog_topup_success.*

class TopupSuccessDialog(private val price: String) : DialogFragment() {

    override fun onResume() {
        super.onResume()
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.dialog_topup_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_price.text = StringUtils.toRp(requireContext(), price)
        tv_action.setOnClickListener { dismiss() }

    }
}