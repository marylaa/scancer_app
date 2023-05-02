package com.example.tom.login_and_register

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.tom.main_page.MainUserActivity
import com.example.tom.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity(), View.OnClickListener {

    private var inputEmail: EditText? = null
    private var inputPassword: EditText? = null
    private var loginButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inputEmail = findViewById(R.id.inputEmail)
        inputPassword = findViewById(R.id.inputPassword)
        loginButton = findViewById(R.id.loginButton)

        loginButton?.setOnClickListener {
            logInRegisteredUser()
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {

                R.id.registerButton -> {
                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {

        return when {
            TextUtils.isEmpty(inputEmail?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(inputPassword?.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                showErrorSnackBar("Poprawnie zalogowano", false)
                true
            }
        }
    }

    private fun logInRegisteredUser() {

        if (validateLoginDetails()) {
            val email = inputEmail?.text.toString().trim() { it <= ' ' }
            val password = inputPassword?.text.toString().trim() { it <= ' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {
                        goToMainActivity()
                        finish()
                    } else {
                        showErrorSnackBar(task.exception!!.message.toString(), true)
                    }
                }
        }
    }

    open fun goToMainActivity() {
        val intent = Intent(this@LoginActivity, MainUserActivity::class.java)
        startActivity(intent)
    }
}