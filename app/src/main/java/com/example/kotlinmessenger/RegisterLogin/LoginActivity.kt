package com.example.kotlinmessenger.RegisterLogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.LatestmessageActivity
import com.example.kotlinmessenger.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle? ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        login_bt_login.setOnClickListener {
            performLogin()
        }
        backtoRegister_tv_login.setOnClickListener {
            val intent=Intent(this,
                RegistrationActivity::class.java)
            startActivity(intent)
        }


    }
    private fun performLogin(){
        val email=email_et_login.text.toString()
        val password=password_et_login.text.toString()
        if(email.isEmpty()||password.isEmpty()){
            Toast.makeText(this,"please fill password/email",Toast.LENGTH_SHORT).show()
        }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { if (!it.isSuccessful) return@addOnCompleteListener

                Log.d("Login", "Successfully logged in: ${it.result?.user?.uid}")

                val intent = Intent(this, LatestmessageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
         .addOnFailureListener {
             Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()
         }
    }
}