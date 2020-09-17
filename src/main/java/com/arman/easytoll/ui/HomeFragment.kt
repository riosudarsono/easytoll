package com.arman.easytoll.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arman.easytoll.R
import com.arman.easytoll.data.model.IdNameData
import com.arman.easytoll.ui.saldo.SaldoActivity
import com.arman.easytoll.utils.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jaredrummler.materialspinner.MaterialSpinner
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment(), BaseApp.Listener {

    private var cardData: IdNameData? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        BaseApp(this).set()
    }

    override fun getIntentData() {

    }

    override fun setOnClick() {
        sp_kartu.setOnItemSelectedListener(
            MaterialSpinner.OnItemSelectedListener<IdNameData> { view, position, id, item ->
                cardData = item
            })
        tv_check.setOnClickListener { onCheck() }
    }

    override fun setAdapter() {
        val list: MutableList<IdNameData> = ArrayList()
        list.add(IdNameData("1", "E-Money"))
        list.add(IdNameData("2", "Flash"))
        list.add(IdNameData("3", "Brizzi"))
        list.add(IdNameData("4", "Ovo"))
        list.add(IdNameData("5", "Gopay"))
        list.add(IdNameData("6", "Dana"))
        list.add(IdNameData("7", "Link Aja"))

        val spinnerAdapter = MaterialSpinnerAdapter<IdNameData>(context, list)
        sp_kartu.setAdapter(spinnerAdapter)
    }

    override fun setContent() {

    }

    override fun loadData() {

    }

    private fun onCheck(){
        val cardNo = input_noKartu.text.toString()
        if (cardData == null){ toast("Silakan Pilih Kartu"); return }
        if (cardNo.isEmpty()){ toast("Nomor Kartu Tidak Boleh Kosong"); return }

        val i = Intent(requireContext(), SaldoActivity::class.java)
        i.putExtra(Constants.DATA_EXTRA, cardNo)
        i.putExtra(Constants.DATA_EXTRA2, cardData)
        startActivity(i)
    }
}