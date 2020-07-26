package com.example.kotlinmessenger

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
//import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.kotlinmessenger.Models.ContactClass
import com.example.kotlinmessenger.Models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.example.kotlinmessenger.R
import com.google.firebase.auth.FirebaseAuth

import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_latestmessage.*
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_newmessage.*
import kotlinx.android.synthetic.main.user_row_newmessage.view.*
import kotlin.coroutines.coroutineContext

class NewMessageActivity : AppCompatActivity() {
   lateinit var cursor:Cursor
     val contacts= ArrayList<ContactClass>()
    val READ_PHONE_PERMISSION_CODE=1

   val latestmessageObj=LatestmessageActivity()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"

        fetchUsers()
        shimmerLayout.startShimmerAnimation()
      //  val checkPermission=ContextCompat.checkSelfPermission(this,android.Manifest.)
       // if(LatestmessageActivity.check)

          checkContactPermission()


    }
    private fun checkContactPermission() {
        val  checkPermission= ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS)
        if(checkPermission== PackageManager.PERMISSION_GRANTED)
        {
            // phoneContactDetails()
            readContact()
        }else
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CONTACTS), READ_PHONE_PERMISSION_CODE)

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            READ_PHONE_PERMISSION_CODE ->{
                if (grantResults.size>=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"permission granted",Toast.LENGTH_SHORT).show()
                    readContact()
                    Log.d("phone no","Contact no:- trying to reed contacts")
                }
                else
                {
                    Toast.makeText(this,"permission Denied",Toast.LENGTH_SHORT).show()
                }
            }

        }
    }



     fun readContact() {
         Log.d("phone no","Contact no:- started read fun contacts")
      cursor=contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null)

         var i=0
         Log.d("phone no","Contact no:- cursure created")
        while (cursor.moveToNext()){
            Log.d("phone no","Contact no:- trying to read contacts while")
            val phoneNumber=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val MnoName=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val contactobj=ContactClass(phoneNumber,MnoName)
//            contactobj.Mno=phoneNumber
//            contactobj.Mname=MnoName

            Log.d("phone no","Contact no:- $phoneNumber")
            contacts.add(contactobj)
            Log.d("phone no","freinds Name -$MnoName")

            i++
        }
         cursor.close()
    }

    companion object {
        val USER_KEY = "USER_KEY"
    }
    fun checkNo( userd:User):Int{
        var result=0
       // contactlistsobj=contacts<ContactClass>()
        Log.d("cheklist","check fun initiated")
        val userMno=userd.phoneNo.replace("\\s".toRegex(),"")
       var No=""
        if (contacts!=null) {

            Log.d("cheklist","array is not null ${userd.phoneNo}")

            for (i in 0..contacts.size-1){
                val num=contacts[i].Mno.replace("\\s".toRegex(),"")
                if (num.length==10) {
                    No = "+91"+num
                }
                else
                {
                    No=num
                }

                Log.d("cheklist", " travesing array trim no $No}")
                if(No==userMno) {
                    Log.d("cheklistMatch", " matched no ${contacts[i].Mno}")
                    result++
                }

            }
        }
        else
        {
            Toast.makeText(this,"array is empty",Toast.LENGTH_SHORT).show()
        }
        return result
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                val curentUserUid=FirebaseAuth.getInstance().uid

                p0.children.forEach {
                    Log.d("NewMessage", it.toString())
                    val user = it.getValue(User::class.java)


                    if (user != null&&user.uid!=curentUserUid) {
                        val res:Int= checkNo(user)
                        if(res>0) {
                            adapter.add(UserItem(user, this@NewMessageActivity))
                            Handler().postDelayed({
                                shimmerLayout.stopShimmerAnimation()
                                shimmerLayout.visibility = View.GONE

                            }, 10)
                        }
                    }
                }

                adapter.setOnItemClickListener { item, view ->

                    val userItem = item as UserItem

                    val intent = Intent(view.context, ChatLogActivity::class.java)
//          intent.putExtra(USER_KEY,  userItem.user.username)
                    intent.putExtra(USER_KEY, userItem.user)
                    startActivity(intent)

                    finish()

                }

                recyclerview_newmessegge.adapter = adapter


            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}

class UserItem(val user: User, val context: Context): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username_row_newmessegw.text = user.username
        viewHolder.itemView.about_row_newmessegw.text=user.aboutUser
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageView_newmessege)
        viewHolder.itemView.imageView_newmessege.setOnClickListener {
            val intent=Intent(context,Profiles::class.java)
            intent.putExtra(NewMessageActivity.USER_KEY,user)
            context.startActivity(intent)
            Toast.makeText(context,"image button clicked",Toast.LENGTH_SHORT).show()
        }
    }

    override fun getLayout(): Int {
        return R.layout.user_row_newmessage
    }
}

