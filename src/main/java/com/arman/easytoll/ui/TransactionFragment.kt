package com.arman.easytoll.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arman.easytoll.R
import com.arman.easytoll.data.model.CardData
import com.arman.easytoll.data.model.HistoryData
import com.arman.easytoll.data.model.IdNameData
import com.arman.easytoll.ui.dialog.TopupSuccessDialog
import com.arman.easytoll.utils.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.jaredrummler.materialspinner.MaterialSpinner
import com.jaredrummler.materialspinner.MaterialSpinnerAdapter
import dagger.hilt.android.AndroidEntryPoint
import id.jsl.autoloka.data.user.UserManager
import kotlinx.android.synthetic.main.fragment_transaction.*
import javax.inject.Inject

@AndroidEntryPoint
class TransactionFragment : Fragment(), BaseApp.Listener {

    @Inject lateinit var userManager: UserManager
    private var database = Firebase.database.reference
    private var dataCard: IdNameData? = null
    private var dataNominal: IdNameData? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        BaseApp(this).set()
    }

    override fun getIntentData() {}

    override fun setOnClick() {
        sp_cardType.setOnItemSelectedListener(
            MaterialSpinner.OnItemSelectedListener<IdNameData> { view, position, id, item ->
                dataCard = item
            })
        sp_nominal.setOnItemSelectedListener(
            MaterialSpinner.OnItemSelectedListener<IdNameData> { view, position, id, item ->
                dataNominal = item
            })
        tv_pay.setOnClickListener { onPay() }
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
        sp_cardType.setAdapter(spinnerAdapter)

        val list2: MutableList<IdNameData> = ArrayList()
        list2.add(IdNameData("5000", "Rp 5.000"))
        list2.add(IdNameData("10000", "Rp 10.000"))
        list2.add(IdNameData("25000", "Rp 25.000"))
        list2.add(IdNameData("50000", "Rp 50.000"))
        list2.add(IdNameData("100000", "Rp 100.000"))
        list2.add(IdNameData("150000", "Rp 150.000"))
        list2.add(IdNameData("200000", "Rp 200.000"))

        val spinnerAdapter2 = MaterialSpinnerAdapter<IdNameData>(context, list2)
        sp_nominal.setAdapter(spinnerAdapter2)
    }

    override fun setContent() {}

    override fun loadData() {}

    private fun onPay(){
        val cardNo = input_cardNo.text.toString()

        if (dataCard == null){ toast("Silakan Pilih Kartu"); return }
        if (dataNominal == null) { toast("Silakan Pilih Nominal") }
        if (cardNo.isEmpty()){ toast("Nomor Kartu Tidak Boleh Kosong"); return }

        progress_bar.show()
        database.child(Constants.CARD).child(dataCard?.id!! + "_$cardNo")
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    toast(error.message)
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val cardData = snapshot.getValue(CardData::class.java)
                        val nominal = cardData?.saldo?.toInt()?.plus(dataNominal?.id!!.toInt())
                        setData(cardNo, dataCard?.name!!, nominal.toString())
                    } else {
                        setData(cardNo, dataCard?.name!!, dataNominal?.id!!)
                    }
                    setHistory(Constants.TOPUP + " " + dataCard?.name, dataNominal?.id!!)
                }

            })
    }

    private fun setData(cardNo: String, cardType: String, nominal: String){
        database.child(Constants.CARD).child(dataCard?.id!! + "_$cardNo")
            .setValue(CardData(cardType, nominal))
            .addOnSuccessListener { toast("success") }
            .addOnCanceledListener { toast("cancel") }
    }

    private fun setHistory(status: String, realNominal: String){
        database.child(Constants.HISTORY).child(userManager.id)
            .push()
            .setValue(HistoryData(status, realNominal, true))
            .addOnSuccessListener {
                val dialog = TopupSuccessDialog(realNominal)
                dialog.show(childFragmentManager, TopupSuccessDialog::class.java.name)
                progress_bar.hide() }
            .addOnCanceledListener { progress_bar.hide() }
    }



}