package com.example.kotlinmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.kotlinmessenger.Models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profiles.*
import kotlinx.android.synthetic.main.user_row_newmessage.view.*

class Profiles : AppCompatActivity() {
 var userDetails:User?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profiles)

        userDetails=intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        Toast.makeText(this,"${userDetails?.email}",Toast.LENGTH_SHORT).show()
        bindData()
        supportActionBar?.title = userDetails?.username

        
    }
    fun bindData(){
        about_profile.text=userDetails?.aboutUser
        userName_profille.text=userDetails?.username
        email_id_profile.text=userDetails?.email
        Picasso.get().load(userDetails?.profileImageUrl).into(userPic_profile)
    }
}
