package com.arman.easytoll.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.arman.easytoll.R
import com.arman.easytoll.utils.BaseApp
import com.arman.easytoll.utils.Constants
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import id.jsl.autoloka.data.user.UserManager
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BaseApp.Listener {

    companion object{
        const val HOME = "home"
        const val TRANSACTION = "transaction"
    }

    @Inject lateinit var userManager: UserManager
    private var doubleBackToExitPressedOnce = false
    private var page: String? = HOME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Fresco.initialize(this)

        BaseApp(this).set()
    }

    override fun getIntentData() {
        if (intent.hasExtra(Constants.DATA_EXTRA)){
            page = intent.getStringExtra(Constants.DATA_EXTRA)
        }
    }

    override fun setOnClick() {}

    override fun setAdapter() {}

    private fun FragmentManager.instantiate(className: String): Fragment {
        return fragmentFactory.instantiate(ClassLoader.getSystemClassLoader(), className)
    }

    override fun setContent() {
        val homeFragment = supportFragmentManager.instantiate(HomeFragment::class.java.name)
        val transFragment = supportFragmentManager.instantiate(TransactionFragment::class.java.name)
        val profileFragment = supportFragmentManager.instantiate(ProfileFragment::class.java.name)

        supportFragmentManager.beginTransaction()
            .add(R.id.frame, homeFragment, "1")
            .show(homeFragment).commit()

        bottom_navigation_view.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.item_home -> if (homeFragment.isAdded) {
                    supportFragmentManager.beginTransaction()
                        .show(homeFragment)
                        .hide(transFragment)
                        .hide(profileFragment)
                        .commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.frame, homeFragment, "1")
                        .show(homeFragment).commit()
                }
                R.id.item_transaction -> if (transFragment.isAdded) {
                    supportFragmentManager.beginTransaction()
                        .hide(homeFragment)
                        .show(transFragment)
                        .hide(profileFragment)
                        .commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.frame, transFragment, "3")
                        .show(transFragment).commit()
                }
                R.id.item_profile -> if (profileFragment.isAdded) {
                    supportFragmentManager.beginTransaction()
                        .hide(homeFragment)
                        .hide(transFragment)
                        .show(profileFragment)
                        .commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .add(R.id.frame, profileFragment, "4")
                        .show(profileFragment).commit()
                }
            }
            true
        })
    }

    override fun loadData() {
        if (page == TRANSACTION){
            bottom_navigation_view.selectedItemId = R.id.item_transaction
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Click BACK again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

}