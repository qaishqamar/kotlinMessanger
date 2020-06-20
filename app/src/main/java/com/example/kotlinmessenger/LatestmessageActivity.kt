package com.example.kotlinmessenger

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.kotlinmessenger.Models.ChatMessage
import com.example.kotlinmessenger.Models.User
import com.example.kotlinmessenger.NewMessageActivity.Companion.USER_KEY
import com.example.kotlinmessenger.RegisterLogin.LoginActivity
import com.example.kotlinmessenger.RegisterLogin.UserResistration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_latestmessage.*
import kotlinx.android.synthetic.main.new_placeholder.view.*

class LatestmessageActivity : AppCompatActivity() {
 companion object{
     var currentUser: User?=null
   //  val USER_KEY="user key"
     val PROF_USER_KEY="pro user key"

 }


    val adapter=GroupAdapter<GroupieViewHolder>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latestmessage)
        supportActionBar?.title = currentUser?.username


        VerifyUser()
        recyclreView_latestMessage.adapter=adapter
        recyclreView_latestMessage.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        fetchCurrentUser()
        adapter.setOnItemClickListener{item, view ->
            val row=item as LatestMessageRow

            val intent=Intent(this,ChatLogActivity::class.java)
            intent.putExtra(USER_KEY,row.chatPartnerUser)
            startActivity(intent)
        }
        goto_resistration.setOnClickListener {
            val intent=Intent(this,UserResistration::class.java)
            startActivity(intent)
        }


       // setDummyData()
        listenForLatestMessage()
        // for shimeractivity
        shimmerLayout_latest.startShimmerAnimation()
    }





    val latesMessagesMap=HashMap<String,ChatMessage>()
    fun refresRecyclerView(){
        adapter.clear()
        latesMessagesMap.values.forEach{

            adapter.add(LatestMessageRow(it))
        }

    }
    private fun  listenForLatestMessage(){
        val fromId=FirebaseAuth.getInstance().uid
        val ref= FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object:ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage=p0.getValue(ChatMessage::class.java)?:return
                latesMessagesMap[p0.key!!]=chatMessage
                refresRecyclerView()
                Handler().postDelayed({
                    shimmerLayout_latest.stopShimmerAnimation()
                    shimmerLayout_latest.visibility= View.GONE
                },50)

            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage=p0.getValue(ChatMessage::class.java)?:return
                latesMessagesMap[p0.key!!]=chatMessage
                refresRecyclerView()
            }
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }
            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })
    }

   //}
    fun fetchCurrentUser(){
        val uid=FirebaseAuth.getInstance().uid
        val ref=FirebaseDatabase.getInstance().getReference("users/$uid")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                currentUser=p0.getValue(User::class.java)
                Log.d("LatestMessage", "current user ${currentUser?.username}")
            }
            override fun onCancelled(p0: DatabaseError) {

            }



        }
        )
    }
    fun VerifyUser(){
        val uid=FirebaseAuth.getInstance().uid
        if(uid==null)
        {
            val intent=Intent(this,
                LoginActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.curent_user_profile->{
                val intent=Intent(this, Profiles::class.java)
                intent.putExtra(USER_KEY, currentUser)
                startActivity(intent)

            }
            R.id.menu_sign_out->{
                FirebaseAuth.getInstance().signOut()
                val intent=Intent(this,
                   LoginActivity::class.java)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            R.id.menu_new_user->{
                val intent=Intent(this,NewMessageActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

}
