package com.example.tom.login_and_register

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.tom.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : BaseActivity() {

    private var registerButton: Button? = null
    private var inputEmail: EditText? = null
    private var inputFirstName: EditText? = null
    private var inputLastName: EditText? = null
    private var inputPassword: EditText? = null
    private var inputRepPass: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        registerButton = findViewById(R.id.registerButton)
        inputEmail = findViewById(R.id.inputEmaill)
        inputFirstName = findViewById(R.id.inputFirstName)
        inputLastName = findViewById(R.id.inputLastName)
        inputPassword = findViewById(R.id.inputPasswordd)
        inputRepPass = findViewById(R.id.inputPassworddRepeat)

        registerButton?.setOnClickListener{
            registerUser()
        }
    }


    private fun validateRegisterDetails(): Boolean {

        return when{
            TextUtils.isEmpty(inputEmail?.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
                false
            }
//            TextUtils.isEmpty(inputFirstName?.text.toString().trim{ it <= ' '}) -> {
//                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name),true)
//                false
//            }
//            TextUtils.isEmpty(inputLastName?.text.toString().trim{ it <= ' '}) -> {
//                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name),true)
//                false
//            }
            TextUtils.isEmpty(inputPassword?.text.toString().trim{ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password),true)
                false
//            }
//            TextUtils.isEmpty(inputRepPass?.text.toString().trim{ it <= ' '}) -> {
//                showErrorSnackBar(resources.getString(R.string.err_msg_enter_reppassword),true)
//                false
//            }
//            inputPassword?.text.toString().trim {it <= ' '} != inputRepPass?.text.toString().trim{it <= ' '} -> {
//                showErrorSnackBar(resources.getString(R.string.err_msg_password_mismatch),true)
//                false
            } else -> {
                //showErrorSnackBar("Your details are valid",false)
                true
            }
        }
    }

    fun goToLogin(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun registerUser(){
        if (validateRegisterDetails()){
            val login: String = inputEmail?.text.toString().trim() {it <= ' '}
            val password: String = inputPassword?.text.toString().trim() {it <= ' '}

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(login,password).addOnCompleteListener(
                OnCompleteListener <AuthResult>{ task ->
                    if(task.isSuccessful){
                        showErrorSnackBar("You are registered successfully",false)

                        FirebaseAuth.getInstance().signOut()
                        finish()
                    } else{
                        showErrorSnackBar(task.exception!!.message.toString(),true)
                    }
                })
        }
    }
}