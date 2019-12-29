package com.example.kotlinmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.kotlinmessenger.Models.ChatMessage
import com.example.kotlinmessenger.Models.User
import com.example.kotlinmessenger.NewMessageActivity.Companion.USER_KEY
import com.example.kotlinmessenger.RegisterLogin.RegistrationActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_latestmessage.*
import kotlinx.android.synthetic.main.latestmessage_row.view.*

class LatestmessageActivity : AppCompatActivity() {
 companion object{
     var currentUser: User?=null
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
       // setDummyData()
        listenForLatestMessage()
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
            }
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage=p0.getValue(ChatMessage::class.java)?:return
                latesMessagesMap[p0.key!!]=chatMessage
                refresRecyclerView()
            }
            override fun onCancelled(p0: DatabaseError) {

            }
            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
                RegistrationActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.menu_sign_out->{
                FirebaseAuth.getInstance().signOut()
                val intent=Intent(this,
                    RegistrationActivity::class.java)
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
