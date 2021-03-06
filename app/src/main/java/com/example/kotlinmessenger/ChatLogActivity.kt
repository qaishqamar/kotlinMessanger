package com.example.kotlinmessenger

//import android.support.v7.app.AppCompatActivity
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
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
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*



open class ChatLogActivity : AppCompatActivity() {
companion object{
   val tag="chatlog"
    val TABLE_NAME = "Chatdata"
     var COLUMN_USER_ID="si"

    val CHAT_MSG = "chatmsg"
    val TIME="time"
    val DATABASE_VERSION=1
    val DATABASE_NAME  ="mychatDb"
    val FIRST_P_ID="senderid"
    val SECOND_P_ID="reciverid"


}

    var toId:String?=""
   var fromId:String?=""
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
            val etext=edittext_chat_log.text.toString()
            if(TextUtils.isEmpty(etext)|| etext.trim().length==0) {
                Toast.makeText(this,"text is empty! \n please insert text",Toast.LENGTH_SHORT).show()
            }
            else
            {
                performSendMessage()

            }
        }
    }
    private fun listenForMessages(){
         fromId=FirebaseAuth.getInstance().uid
         toId=toUser?.uid

        val ref=FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        ref.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage=p0.getValue(ChatMessage::class.java)


                if(chatMessage!=null) {
                    if(chatMessage.fromId==FirebaseAuth.getInstance().uid) {
                        Log.d(tag, chatMessage.text)
                        val currentUser=LatestmessageActivity.currentUser



                        adapter.add(ChatFromItem(chatMessage,currentUser!!,this@ChatLogActivity))
                    }
                    else{

                        adapter.add(ChatToItem(chatMessage,toUser!!,this@ChatLogActivity))

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

class ChatFromItem(val chatMessage: ChatMessage,val user: User,val context: Context): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
     viewHolder.itemView.textView_from_chat.text=chatMessage.text
        val firstFchar:Char=user.username.get(0)
     //viewHolder.itemView.user_name_from_chat.text=firstFchar.toString()
        val trimName=user.username.trim()
       // viewHolder.itemView.user_from_chat.text=trimName.toLowerCase()
        val toId=user.uid
       val showtoast:MessageDeleteUpdate=MessageDeleteUpdate(viewHolder.itemView.textView_from_chat,context)

        viewHolder.itemView.textView_from_chat.setOnLongClickListener {

           showtoast.showPopup(chatMessage)

               true
        }
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem(val chatMessage: ChatMessage,val user:User,val context: Context): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
     viewHolder.itemView.textView_to_chat.text=chatMessage.text
      val firstTochar:Char=user.username.get(0)
        val toId=user.uid
        val obj= MessageDeleteUpdate(viewHolder.itemView.textView_to_chat,context)
        viewHolder.itemView.setOnLongClickListener {
            obj.showPopup(chatMessage)
         true
        }

    // viewHolder.itemView.user_name_to_chat.text=firstTochar.toString()
    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}

