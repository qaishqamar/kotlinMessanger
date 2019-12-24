package com.example.kotlinmessenger.RegisterLogin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.kotlinmessenger.LatestmessageActivity
import com.example.kotlinmessenger.Models.User
import com.example.kotlinmessenger.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.*

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        register_bt_rester.setOnClickListener {
            performRegister()
        }
        alredy_have_acount_tv_register.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        SelectImage_button_register.setOnClickListener {
            Log.d("Main","Try to show photo selecter")
            val intent=Intent(Intent.ACTION_PICK)
            intent.type="image/*"
            startActivityForResult(intent,0)
        }
    }
    var selected_photo_uri: Uri? =null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0&&resultCode== Activity.RESULT_OK&&data!=null){
            //proceed and check what thee image is selected
            Log.d("Main","image is selected")
            selected_photo_uri=data.data
            val bitmap=MediaStore.Images.Media.getBitmap(contentResolver,selected_photo_uri)
            select_circleImageView_register.setImageBitmap(bitmap)
            SelectImage_button_register.alpha=0f
         //   val bitmapDrawable=BitmapDrawable(bitmap)
         //   SelectImage_button_register.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun performRegister(){
        val email = email_et_register.text.toString()
        val password = password_et_register.text.toString()
        Log.d("RegistrationActivity", "registring email: $email")
        Log.d("RegistrationActivity", "registring password: $password")
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "enter email or password", Toast.LENGTH_SHORT).show()
            return
        }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                // else part
                Log.d("Main", "succesfully created user wiith id :${it.result?.user?.uid}")
                uploadImagrToFirebase()


            }
            .addOnFailureListener {
                Toast.makeText(
                    this, "failed to create user acount:${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
  private fun uploadImagrToFirebase(){
     val filename= UUID.randomUUID().toString()
      val ref= FirebaseStorage.getInstance().getReference("/image/$filename")
      ref.putFile(selected_photo_uri!!)
          .addOnSuccessListener {
              Log.d("Main","image is uploaded sucessfully ${it.metadata?.path}")
              ref.downloadUrl.addOnSuccessListener {
                  Log.d("Main","File location :$it")

                  saveUserToFirebase(it.toString())
              }
          }
          .addOnFailureListener{
              //todo
          }

 }
    private fun saveUserToFirebase(profileImageUrl:String){
        val uid=FirebaseAuth.getInstance().uid?:""
        val user= User(
            uid.toString(),
            userName_et_register.text.toString(),
            profileImageUrl
        )
       val ref=FirebaseDatabase.getInstance().getReference("/users/$uid")

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Main","user detail is uploaded")
                val intent=Intent(this,
                    LatestmessageActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }

            .addOnFailureListener {
                Log.d("Main","details re not uploaded :try again")
                Toast.makeText(this,"users detail not uploaded",Toast.LENGTH_SHORT).show()

            }
    }
}

//FirebaseFirestore.getInstance().document("/users/$uid").set(user)"