package com.arman.easytoll.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arman.easytoll.R
import com.arman.easytoll.utils.BaseApp
import com.arman.easytoll.utils.hide
import com.arman.easytoll.utils.show
import com.arman.easytoll.utils.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.activity_register.*


class RegisterActivity : AppCompatActivity(), BaseApp.Listener {

    private val mAuth: FirebaseAuth? = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        BaseApp(this).set()
    }

    override fun getIntentData() {}

    override fun setOnClick() {
        iv_back.setOnClickListener { onBackPressed() }
        tv_register.setOnClickListener { register() }
    }

    override fun setAdapter() {

    }

    override fun setContent() {

    }

    override fun loadData() {

    }

    private fun register(){
        val name = input_name.text.toString()
        val email = input_username.text.toString()
        val password = input_password.text.toString()

        if (name.isEmpty()){ toast("Name cannot Empty"); return }
        if (email.isEmpty()){ toast("Email cannot Empty"); return }
        if (password.isEmpty()){ toast("Password cannot Empty"); return }

        progress_bar.show()
        mAuth?.createUserWithEmailAndPassword(email, password)?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name).build()
                val user = mAuth.currentUser
                user?.updateProfile(profileUpdates)
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            } else {
                toast(task.exception?.message)
            }
            progress_bar.hide()
        }
    }
}