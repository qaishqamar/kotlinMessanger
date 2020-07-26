package com.example.kotlinmessenger

import android.content.ClipData
import android.content.Context
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.WindowId
import android.widget.PopupMenu
import android.widget.Toast
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*

import kotlinx.android.synthetic.main.delete_toast_layout.*
import java.util.zip.Inflater

class ShowToast(val textId: View, val context: Context) {

//    fun customToastshow():Boolean {
//        val layout = layoutInflater.inflate(R.layout.delete_toast_layout, custom_layout_de)
//        val myToast = Toast(applicationContext)
//        myToast.duration=Toast.LENGTH_LONG
//        myToast?.view = layout
//        myToast.setGravity(Gravity.CENTER, 0, 0)
//
//        myToast.show()
//        return true
//    }
    fun showPopup(){
    val popupMenu= PopupMenu(context,textId)
    popupMenu.menuInflater.inflate(R.menu.menu_pop,popupMenu.menu)
    popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when(item!!.itemId){
                R.id.edit_msg_menu->{
                    Toast.makeText(context,"edit is clicked",Toast.LENGTH_SHORT).show()
                }
                R.id.delet_msg_menu->{
                    Toast.makeText(context,"Delete is clicked",Toast.LENGTH_SHORT).show()
                }
            }
            return true
        }

    })
    popupMenu.show()


}

}