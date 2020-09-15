package com.arman.easytoll.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.arman.easytoll.R
import com.arman.easytoll.data.model.LoginData
import com.arman.easytoll.ui.MainActivity
import com.arman.easytoll.utils.BaseApp
import com.arman.easytoll.utils.hide
import com.arman.easytoll.utils.show
import com.arman.easytoll.utils.toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import id.jsl.autoloka.data.user.UserManager
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), BaseApp.Listener {

    companion object {
        const val REQ_LOGIN_GOOGLE = 2
    }

    @Inject lateinit var userManager: UserManager
    private val viewModel: LoginViewModel by viewModels()

    private val mAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private lateinit var gso: GoogleSignInOptions
    private var googleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        BaseApp(this).set()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == REQ_LOGIN_GOOGLE){
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                handleSignInResult(task)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun getIntentData() {

    }

    override fun setOnClick() {
        tv_signIn.setOnClickListener { login() }
        ly_google.setOnClickListener {
            progress_bar.show()
            val signInIntent: Intent? = googleSignInClient?.signInIntent
            startActivityForResult(signInIntent, REQ_LOGIN_GOOGLE)
        }
    }

    override fun setAdapter() {

    }

    override fun setContent() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun loadData() {

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            mAuth?.signInWithCredential(credential)
                ?.addOnCompleteListener(this, OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser? = mAuth.currentUser
                        userManager.loginSaveUserData(LoginData(user?.uid, user?.displayName, user?.email, user?.photoUrl.toString()))
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        toast(task.exception?.message)
                    }
                    progress_bar.hide()
                })
        } catch (e: ApiException) {
            Log.w("TAG", "signInResult:failed code=" + e.statusCode)
            progress_bar.hide()
        }
    }

    private fun login(){
        val email = input_username.text.toString()
        val password = input_password.text.toString()

        if (email.isEmpty()){ toast("Email cannot Empty"); return }
        if (password.isEmpty()){ toast("Password cannot Empty"); return }

        progress_bar.show()
        mAuth?.signInWithEmailAndPassword(email, password)?.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = mAuth.currentUser
                userManager.loginSaveUserData(LoginData(user?.uid, user?.displayName, user?.email, user?.photoUrl.toString()))
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                toast(task.exception?.message)
            }
            progress_bar.hide()
        }
    }

}