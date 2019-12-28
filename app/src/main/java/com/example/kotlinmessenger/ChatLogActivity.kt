package com.example.kotlinmessenger

//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmessenger.Models.ChatMessage
import com.example.kotlinmessenger.Models.User

import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

import kotlinx.android.synthetic.main.activity_chat_log.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*


class ChatLogActivity : AppCompatActivity() {
companion object{
   val tag="chatlog"
}
    val adapter=GroupAdapter<GroupieViewHolder>()
    var toUser:User?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)
        recyclerview_chat_log.adapter=adapter

        toUser=intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = toUser?.username

        listenForMessages()
        send_button_chat_log.setOnClickListener {
            Log.d(tag,"send button clicked")
            performSendMessage()
        }
    }
    private fun listenForMessages(){
        val fromId=FirebaseAuth.getInstance().uid
        val toId=toUser?.uid
        val ref=FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        ref.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage=p0.getValue(ChatMessage::class.java)
                if(chatMessage!=null) {
                    if(chatMessage.fromId==FirebaseAuth.getInstance().uid) {
                        Log.d(tag, chatMessage.text)
                        val currentUser=LatestmessageActivity.currentUser
                        adapter.add(ChatFromItem(chatMessage.text,currentUser!!))
                    }
                    else{

                        adapter.add(ChatToItem(chatMessage.text,toUser!!))

                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })

    }

    private fun performSendMessage(){
        val user=intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId=user.uid
        val fromId=FirebaseAuth.getInstance().uid
        val text=edittext_chat_log.text.toString()
        if(fromId==null)return

       //  val reference=FirebaseDatabase.getInstance().getReference("/message").push()
        val reference=FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toReference=FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
        val message=ChatMessage(reference.key!!,toId,text,fromId,System.currentTimeMillis()/1000)
        reference.setValue(message)
            .addOnSuccessListener {
                Log.d(tag,"message has sent")
                edittext_chat_log.text.clear()
                recyclerview_chat_log.scrollToPosition(adapter.itemCount -1)

            }
        toReference.setValue(message)
        val latestMessageRef=FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessageRef.setValue(message)
        val latestMessageToRef=FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
         latestMessageToRef.setValue(message)
    }

}

class ChatFromItem(val text:String,val user: User): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
     viewHolder.itemView.textView_from_chat.text=text
     Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageView_From_Chat)
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem(val text: String,val user:User): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
     viewHolder.itemView.textView_to_chat.text=text
     val  uri= user.profileImageUrl
     val targetImageView=viewHolder.itemView.imageView_To_Chat
     Picasso.get().load(uri).into(targetImageView)
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}