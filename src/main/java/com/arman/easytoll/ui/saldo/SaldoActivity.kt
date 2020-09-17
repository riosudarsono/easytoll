package com.arman.easytoll.ui.saldo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.util.StringUtil
import com.arman.easytoll.R
import com.arman.easytoll.data.model.CardData
import com.arman.easytoll.data.model.IdNameData
import com.arman.easytoll.ui.MainActivity
import com.arman.easytoll.utils.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_saldo.*
import kotlinx.android.synthetic.main.activity_saldo.progress_bar
import kotlinx.android.synthetic.main.fragment_home.*

class SaldoActivity : AppCompatActivity(), BaseApp.Listener {

    private var database = Firebase.database.reference
    private var cardNo: String? = null
    private var cardData: IdNameData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saldo)

        BaseApp(this).set()
    }

    override fun getIntentData() {
        if (intent.hasExtra(Constants.DATA_EXTRA)){
            cardNo = intent.getStringExtra(Constants.DATA_EXTRA)
            cardData = intent.getParcelableExtra(Constants.DATA_EXTRA2)
        } else {
            toast(getString(R.string.empty_data))
            finish()
        }
    }

    override fun setOnClick() {
        iv_back.setOnClickListener { onBackPressed() }
        tv_topUp.setOnClickListener {
            val i = Intent(this, MainActivity::class.java)
            i.putExtra(Constants.DATA_EXTRA, MainActivity.TRANSACTION)
            startActivity(i)
            finishAffinity()
        }
    }

    override fun setAdapter() {}

    override fun setContent() {
        tv_cardName.text = cardData?.name
        tv_cardNo.text = cardNo
    }

    override fun loadData() {
        progress_bar.show()
        database.child(Constants.CARD).child(cardData?.id!! + "_$cardNo")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    toast(error.message)
                    progress_bar.hide()
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val mCardData = snapshot.getValue(CardData::class.java)
                        tv_saldo.text = StringUtils.toRp(this@SaldoActivity, mCardData?.saldo)
                    } else {
                        tv_saldo.text = StringUtils.toRp(this@SaldoActivity, "0")
                    }
                    ly_content.show()
                    progress_bar.hide()
                }
            })
    }
}