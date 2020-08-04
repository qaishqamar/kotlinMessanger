package com.example.kotlinmessenger.RegisterLogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.LatestmessageActivity
import com.example.kotlinmessenger.Models.User
import com.example.kotlinmessenger.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.TimeUnit

class LoginActivity:AppCompatActivity(){
    companion object{
        lateinit var phonNoStatic:String
    }
    lateinit var mAuth: FirebaseAuth
    val TAG="signin"
    var phonNo=""
    var verificationId=""
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    override fun onCreate(savedInstanceState: Bundle? ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance()

        phone_login_btn.setOnClickListener {
            if(phone_no_et.length() ==10){
                verify()
            }
            else
            {
                Toast.makeText(this,"enter no. properly" +
                        "you have enterd onl:${phone_no_et.length()} numbers ",Toast.LENGTH_SHORT).show()

            }

        }
        login_otp_btn.setOnClickListener {
           authenticate()
        }


//        login_bt_login.setOnClickListener {
//            performLogin()
//        }
//        backtoRegister_tv_login.setOnClickListener {
//            val intent=Intent(this,
//                RegistrationActivity::class.java)
//            startActivity(intent)
//        }


    }
    private fun verificationCallback(){
         mCallbacks=object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
             override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                 Toast.makeText(this@LoginActivity,"verification succesfull",Toast.LENGTH_SHORT).show()
              signInWithPhoneAuthCredential(credential)
             }

             override fun onVerificationFailed(p0: FirebaseException) {
                 Toast.makeText(this@LoginActivity,"verification failed",Toast.LENGTH_SHORT).show()
                 Log.d("auth","phone no is not authenticate")
             }

             override fun onCodeSent(verification: String, p1: PhoneAuthProvider.ForceResendingToken) {
                 super.onCodeSent(verification, p1)
                 verificationId=verification
                get_mobile_no_layput.visibility= android.view.View.GONE
//
              get_otp_layout.visibility=android.view.View.VISIBLE
             }

         }

    }
    private fun verify(){
        phonNo="+91"+phone_no_et.text.toString()
        phonNoStatic=phonNo
        verificationCallback()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phonNo,
            60,
            TimeUnit.SECONDS,
            this,
            mCallbacks
        )


    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    startActivity(Intent(this,UserResistration::class.java))
                   // val user = task.result?.user
                   // Log.d(TAG,"My $user" )
                    // ...
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                }
            }
    }
    fun authenticate(){
        val verifiNo=get_otp_et.text.toString()
        val credential:PhoneAuthCredential=PhoneAuthProvider.getCredential(verificationId,verifiNo)

            signInWithPhoneAuthCredential(credential)


    }
    //

//    private fun performLogin(){
//        val email=email_et_login.text.toString()
//        val password=password_et_login.text.toString()
//        if(email.isEmpty()||password.isEmpty()){
//            Toast.makeText(this,"please fill password/email",Toast.LENGTH_SHORT).show()
//        }
//        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
//            .addOnCompleteListener { if (!it.isSuccessful) return@addOnCompleteListener
//
//                Log.d("Login", "Successfully logged in: ${it.result?.user?.uid}")
//
//                val intent = Intent(this, LatestmessageActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(intent)
//
//            }
//         .addOnFailureListener {
//             Toast.makeText(this, "Failed to log in: ${it.message}", Toast.LENGTH_SHORT).show()
//         }
//    }
}






