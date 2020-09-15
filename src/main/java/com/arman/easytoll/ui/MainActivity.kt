package com.arman.easytoll.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arman.easytoll.R
import com.arman.easytoll.utils.BaseApp
import dagger.hilt.android.AndroidEntryPoint
import id.jsl.autoloka.data.user.UserManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BaseApp.Listener {

    @Inject lateinit var userManager: UserManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BaseApp(this).set()
    }

    override fun getIntentData() {

    }

    override fun setOnClick() {

    }

    override fun setAdapter() {

    }

    override fun setContent() {

    }

    override fun loadData() {

    }

}