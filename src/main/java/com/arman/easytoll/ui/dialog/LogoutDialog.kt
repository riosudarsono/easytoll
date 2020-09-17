package com.arman.easytoll.ui.dialog


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.arman.easytoll.R
import kotlinx.android.synthetic.main.dialog_logout.*

class LogoutDialog(private val message: String, private val listener: Listener) : DialogFragment() {

    override fun onResume() {
        super.onResume()
        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.dialog_logout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_text.text = message

        tv_cancel.setOnClickListener { dismiss() }
        tv_action.setOnClickListener {
            listener.onDialogLogout()
            dismiss()
        }
    }

    interface Listener {
        fun onDialogLogout()
    }

}