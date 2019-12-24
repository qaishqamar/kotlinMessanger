package com.example.kotlinmessenger.RegisterLogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle? ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        login_bt_login.setOnClickListener {
            val email=email_et_login.text.toString()
            val password=password_et_login.text.toString()
            Log.d("LoginActivity","login email :$email")
            Log.d("LoginActivity","login password :$password")
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
             //   .addOnCompleteListener {  }
              //  .addOnFailureListener {  }
        }
        backtoRegister_tv_login.setOnClickListener {
            val intent=Intent(this,
                RegistrationActivity::class.java)
            startActivity(intent)
        }


    }
}