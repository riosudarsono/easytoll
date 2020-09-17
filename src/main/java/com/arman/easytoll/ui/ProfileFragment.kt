package com.arman.easytoll.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.arman.easytoll.R
import com.arman.easytoll.data.model.HistoryData
import com.arman.easytoll.ui.adapter.HistoryAdapter
import com.arman.easytoll.ui.auth.LoginActivity
import com.arman.easytoll.ui.dialog.LogoutDialog
import com.arman.easytoll.utils.BaseApp
import com.arman.easytoll.utils.Constants
import com.arman.easytoll.utils.Utils
import com.arman.easytoll.utils.toast
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.SnapshotHolder
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import id.jsl.autoloka.data.user.UserManager
import kotlinx.android.synthetic.main.fragment_profile.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(), BaseApp.Listener, LogoutDialog.Listener {

    @Inject lateinit var userManager: UserManager
    private var database = Firebase.database.reference
    private var historyData: MutableList<HistoryData> = ArrayList()
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        BaseApp(this).set()
    }

    override fun getIntentData() {

    }

    override fun setOnClick() {
        tv_logout.setOnClickListener {
            val dialog = LogoutDialog("Are you sure logout?", this)
            dialog.show(childFragmentManager, LogoutDialog::class.java.name)
        }
        iv_photo.setOnClickListener { Utils.imageOverLay1(requireContext(), userManager.photo) }
    }

    override fun setAdapter() {
        historyAdapter = HistoryAdapter(requireActivity(), ArrayList())
        rv_history.adapter = historyAdapter
        rv_history.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    override fun setContent() {
        Utils.setGlideCircle(requireContext(), userManager.photo, iv_photo)
        tv_name.text = userManager.name
        tv_email.text = userManager.email
    }

    override fun loadData() {
        database.child(Constants.HISTORY).child(userManager.id)
            .addValueEventListener(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    toast("Cancel")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    historyData.clear()
                    for (data in snapshot.children){
                        val list = data.getValue(HistoryData::class.java)
                        historyData.add(list!!)
                    }
                    historyAdapter.setItems(historyData)
                }
            })
    }

    override fun onDialogLogout() {
        userManager.logout()
        FirebaseAuth.getInstance().signOut();
        activity?.finishAffinity()
        startActivity(Intent(activity, LoginActivity::class.java))
    }
}