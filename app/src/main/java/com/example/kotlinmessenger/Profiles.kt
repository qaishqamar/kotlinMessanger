package com.example.kotlinmessenger

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.kotlinmessenger.LatestmessageActivity.Companion.currentUser
import com.example.kotlinmessenger.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profiles.*
import java.util.*

class Profiles : AppCompatActivity() {
    var userDetails: User? = null
    val uid= FirebaseAuth.getInstance().uid?:""
    var editingValue:String?=null
    var useChildTitle:String=""
    val refd =FirebaseDatabase.getInstance().getReference("/users/$uid")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profiles)

        userDetails = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        Toast.makeText(this, "${userDetails?.email}", Toast.LENGTH_SHORT).show()
        bindData()
        supportActionBar?.title = userDetails?.username
        if(userDetails?.uid!=currentUser?.uid){
            photo_pic_profile.visibility= View.GONE
            name_edit_iv.visibility=View.GONE
            email_edit_iv.visibility=View.GONE
            about_edit_iv.visibility=View.GONE
        }
        photo_pic_profile.setOnClickListener {
            Log.d("Main", "Try to show photo selecter")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
            useChildTitle="profileImageUrl"

        }
        name_edit_iv.setOnClickListener {
          text_view_etg_lay.setText("Enter your Name")
          editingValue=userDetails?.username
          edit_text_etg_lay.setText(editingValue)
          editing_layout_prof.visibility=View.VISIBLE
            useChildTitle="username"

        }
        email_edit_iv.setOnClickListener {
            text_view_etg_lay.setText("Enter Your Email")
            editingValue=userDetails?.email
            edit_text_etg_lay.setText(editingValue)
            editing_layout_prof.visibility=View.VISIBLE
            useChildTitle="email"
        }
        about_edit_iv.setOnClickListener {
            text_view_etg_lay.setText("Enter about you")
            editingValue=userDetails?.aboutUser
            edit_text_etg_lay.setText(editingValue)
            editing_layout_prof.visibility=View.VISIBLE
            useChildTitle="aboutUser"
        }
        save_edit_btn_prof.setOnClickListener {
            val editedData:String=edit_text_etg_lay.text.toString()
            val editingTittle:String=text_view_etg_lay.text.toString()
            if(editedData.trim().length!=0&&editedData!=editingValue){
                when(editingTittle){
                    "Enter your Name"->{ Toast.makeText(this,"editing "+editedData,Toast.LENGTH_SHORT).show()
                        saveImageTofirebasse(editedData,useChildTitle)
                    }


                    "Enter Your Email"->{Toast.makeText(this,"editing "+editedData,Toast.LENGTH_SHORT).show()
                        saveImageTofirebasse(editedData,useChildTitle)
                    }

                    else->{Toast.makeText(this,"editing "+editedData,Toast.LENGTH_SHORT).show()
                        saveImageTofirebasse(editedData,useChildTitle)
                    }

                }

            }
            else{
                Toast.makeText(this,"Please Update "+editingValue ,Toast.LENGTH_SHORT).show()
            }
        }
        cancel_btn_prof.setOnClickListener {
            editing_layout_prof.visibility=View.GONE
        }






    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            //proceed and check what thee image is selected
            Log.d("Main", "image is selected")
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            userPic_profile.setImageBitmap(bitmap)
           // SelectImage_button_register.alpha = 0f
            //   val bitmapDrawable=BitmapDrawable(bitmap)
            //   SelectImage_button_register.setBackgroundDrawable(bitmapDrawable)
           deleteImage()
        }
    }
   private fun deleteImage(){
       val filename=userDetails?.profileImageUrl
       if(filename!=null&&filename.trim().length>0){
           val photoRef=FirebaseStorage.getInstance().getReferenceFromUrl(filename)
           photoRef.delete().addOnSuccessListener {
               Toast.makeText(this,"Image is deleted",Toast.LENGTH_SHORT).show()
               uploadImagrToFirebase()
           }
               .addOnFailureListener {
                   Toast.makeText(this,"Image deletion Failed",Toast.LENGTH_SHORT).show()
                  // uploadImagrToFirebase()
               }
       }else{
           uploadImagrToFirebase()
       }



   }

    private fun uploadImagrToFirebase() {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("Main", "image is uploaded sucessfully ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("Main", "File location :$it")

                   saveImageTofirebasse(it.toString(),"profileImageUrl")
                }
            }
            .addOnFailureListener {
                //todo
            }
    }
        fun bindData() {
            about_profile.text = userDetails?.aboutUser
            userName_profille.text = userDetails?.username
            email_id_profile.text = userDetails?.email
            if (userDetails?.profileImageUrl!=null){
                Picasso.get().load(userDetails?.profileImageUrl).into(userPic_profile)
            }
            else{
                userPic_profile.setImageResource(R.drawable.profile_pic)
            }
        }
     private fun saveImageTofirebasse( value:String,childTitle:String){

         val ref =FirebaseDatabase.getInstance().getReference("/users/").child(uid)
         ref.child(childTitle).setValue(value).addOnSuccessListener {
             Toast.makeText(this,"$useChildTitle is updated successfully",Toast.LENGTH_SHORT).show()
             val intent=Intent(this,LatestmessageActivity::class.java)
             startActivity(intent)
         }
             .addOnFailureListener {
                 Toast.makeText(this,"Image url is saving Failed",Toast.LENGTH_SHORT).show()
             }

    }
//    private fun updateDataInfirebase(){
//        ref.child("profileImageUrl").setValue(imageur).addOnSuccessListener {
//            Toast.makeText(this,"Image url is saved on fb",Toast.LENGTH_SHORT).show()
//            val intent=Intent(this,LatestmessageActivity::class.java)
//            startActivity(intent)
//    }

}
