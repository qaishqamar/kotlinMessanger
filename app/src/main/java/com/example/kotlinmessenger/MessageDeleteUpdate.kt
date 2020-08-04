package com.example.kotlinmessenger

import android.app.AlertDialog
import android.content.ClipData
import android.content.Context
import android.view.*
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.set
import com.example.kotlinmessenger.Models.ChatMessage
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.custom_alert.*
import kotlinx.android.synthetic.main.custom_alert.view.*


import java.util.zip.Inflater

class MessageDeleteUpdate(val textId: View, val context: Context):ChatLogActivity() {


    fun showPopup( chatMessage: ChatMessage){
         val popupMenu= PopupMenu(context,textId)
         popupMenu.menuInflater.inflate(R.menu.menu_pop,popupMenu.menu)
         popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
           override fun onMenuItemClick(item: MenuItem?): Boolean {
              when(item!!.itemId){
                  R.id.edit_msg_menu->{

                    Toast.makeText(context,"edit is clicked",Toast.LENGTH_SHORT).show()
                    showCustomAlert(chatMessage)

                  }
                  R.id.delet_msg_menu->{
                    Toast.makeText(context,"Delete is clicked",Toast.LENGTH_SHORT).show()
                    val mAlertDailogBuilder=AlertDialog.Builder(context)
                    mAlertDailogBuilder.setMessage("Are you sure want to \n delete?")
                    mAlertDailogBuilder.setCancelable(false)
                    mAlertDailogBuilder.setPositiveButton("Yes"){_,_->
                       Toast.makeText(context,"delete button is clicked its under devolopment",Toast.LENGTH_SHORT).show()
                        deleteMsg(chatMessage)

                    }
                    mAlertDailogBuilder.setNeutralButton("Cancel"){_,_->
                        Toast.makeText(context,"Cancel button clicked but its under devolopment",Toast.LENGTH_SHORT).show()
                    }
                    val mAlertDailog=mAlertDailogBuilder.create()
                    mAlertDailog.show()
                }

            }
            return true
        }

    })
    popupMenu.show()


}
   /*
     Function for custom alert of edit massege
    */

    fun showCustomAlert(chatMessage: ChatMessage) {
        val inflate_view=LayoutInflater.from(context).inflate(R.layout.custom_alert,null)

        val textView=inflate_view.findViewById<TextView>(R.id.textViewEdit_alert)as TextView
        val userMsg=inflate_view.findViewById<EditText>(R.id.editTextAlert)as EditText
        userMsg.setText(chatMessage.text)
        val  alertDialog=AlertDialog.Builder(context)
//        alertDialog.setTitle("Change Text.")
        alertDialog.setView(inflate_view)
        alertDialog.setCancelable(true)
        alertDialog.setNegativeButton("Cancel"){dailog,which->
            Toast.makeText(context,"Cancel button clicked but its under devolopment",Toast.LENGTH_SHORT).show()
        }
        alertDialog.setPositiveButton("Done"){dailog,which->
            Toast.makeText(context,"Cancel button clicked but its under devolopment",Toast.LENGTH_SHORT).show()
            val editedMsg=userMsg.toString()
            if (userMsg.equals(chatMessage.text)&&userMsg.toString().trim()==null){
                Toast.makeText(context,"Cancel button clicked but its under devolopment",Toast.LENGTH_SHORT).show()
            }
            else{
                editMsgAlert(chatMessage)
            }



        }
        val dialog=alertDialog.create()
        dialog.show()
    }

    fun  editMsgAlert(chatMessage: ChatMessage){
        val ref =FirebaseDatabase.getInstance().getReference()

    }

    /*
     for deleting message of delete msg alert

     */
    private fun deleteMsg(chatMessage: ChatMessage) {
        val toId=chatMessage.toId
        val fromId=chatMessage.fromId
        val id=chatMessage.id
        val refrom=FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        val refTo=FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId")
         refTo.child(id).removeValue().addOnSuccessListener {

             Toast.makeText(context,"message delete succesfully",Toast.LENGTH_SHORT).show()

         }
             .addOnFailureListener { Toast.makeText(context,"message not deleted ",Toast.LENGTH_SHORT).show() }
        refrom.child(id).removeValue().addOnSuccessListener {
            Toast.makeText(context,"message delete succesfully",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { Toast.makeText(context,"message not deleted ",Toast.LENGTH_SHORT).show() }


    }

}




















