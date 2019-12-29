package com.example.kotlinmessenger

import com.example.kotlinmessenger.Models.ChatMessage
import com.example.kotlinmessenger.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.latestmessage_row.view.*

class LatestMessageRow(val chatMessage: ChatMessage): Item<GroupieViewHolder>(){
    var chatPartnerUser:User?=null
    override fun getLayout(): Int {
        return R.layout.latestmessage_row
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.latest_message_tv_row.text=chatMessage.text
        val chatPartnerId:String
        if(chatMessage.fromId== FirebaseAuth.getInstance().uid)
            chatPartnerId=chatMessage.toId
        else
            chatPartnerId=chatMessage.fromId

        val ref= FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                chatPartnerUser=p0.getValue(User::class.java)
                viewHolder.itemView.usename_latestmessage_row_tv.text=chatPartnerUser?.username
                val target=viewHolder.itemView.imageView
                Picasso.get().load(chatPartnerUser?.profileImageUrl).into(target)
            }
            override fun onCancelled(p0: DatabaseError) {
            }


        })

    }

}
