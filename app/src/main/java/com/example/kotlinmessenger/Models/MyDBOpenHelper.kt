package com.example.kotlinmessenger.Models

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.kotlinmessenger.ChatLogActivity.Companion.CHAT_MSG
import com.example.kotlinmessenger.ChatLogActivity.Companion.COLUMN_USER_ID
import com.example.kotlinmessenger.ChatLogActivity.Companion.DATABASE_NAME
import com.example.kotlinmessenger.ChatLogActivity.Companion.DATABASE_VERSION
import com.example.kotlinmessenger.ChatLogActivity.Companion.FIRST_P_ID
import com.example.kotlinmessenger.ChatLogActivity.Companion.SECOND_P_ID
import com.example.kotlinmessenger.ChatLogActivity.Companion.TABLE_NAME
import com.example.kotlinmessenger.ChatLogActivity.Companion.TIME

//class MyDBOpenHelper(context: Context) :SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
//
//    override fun onCreate(db: SQLiteDatabase?) {
//         val SQL_CREATE_ENTRIES = "CREATE TABLE" + TABLE_NAME + "(" +
//                 COLUMN_USER_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"+
//                 TIME + " INTEGER," +
//                 CHAT_MSG + "TEXT,"+
//                 SECOND_P_ID + "VARCHAR,"
//                 FIRST_P_ID + "VARCHAR"+ ")";
//            db!!.execSQL(SQL_CREATE_ENTRIES)
//        Log.d("db","data inserted ")
//
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//     db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
//        onCreate(db )
//    }
//    fun insertData(chat:ChatMessage,context: Context) {
//        val num = -1
//        val db: SQLiteDatabase = this.writableDatabase
//        val contentValues = ContentValues()
//        contentValues.put(COLUMN_USER_ID, num)
//        contentValues.put(CHAT_MSG, chat.text)
//        contentValues.put(FIRST_P_ID, chat.fromId)
//        contentValues.put(SECOND_P_ID, chat.toId)
//        contentValues.put(TIME, chat.timeStamp)
//        var result = db.insert(TABLE_NAME, null, contentValues)
//        db.close()
//        Log.d("db", "data inserted ${chat.toId}")
//        if (result == -1.toLong()) { Toast.makeText(context, "data insertion failed", Toast.LENGTH_SHORT).show() }
//        else { Toast.makeText(context, "data inserted succesfully", Toast.LENGTH_SHORT).show() }
//    }
//
//
//}//class MyDBOpenHelper(context: Context) :SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
//
//    override fun onCreate(db: SQLiteDatabase?) {
//         val SQL_CREATE_ENTRIES = "CREATE TABLE" + TABLE_NAME + "(" +
//                 COLUMN_USER_ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"+
//                 TIME + " INTEGER," +
//                 CHAT_MSG + "TEXT,"+
//                 SECOND_P_ID + "VARCHAR,"
//                 FIRST_P_ID + "VARCHAR"+ ")";
//            db!!.execSQL(SQL_CREATE_ENTRIES)
//        Log.d("db","data inserted ")
//
//    }
//
//    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//     db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
//        onCreate(db )
//    }
//    fun insertData(chat:ChatMessage,context: Context) {
//        val num = -1
//        val db: SQLiteDatabase = this.writableDatabase
//        val contentValues = ContentValues()
//        contentValues.put(COLUMN_USER_ID, num)
//        contentValues.put(CHAT_MSG, chat.text)
//        contentValues.put(FIRST_P_ID, chat.fromId)
//        contentValues.put(SECOND_P_ID, chat.toId)
//        contentValues.put(TIME, chat.timeStamp)
//        var result = db.insert(TABLE_NAME, null, contentValues)
//        db.close()
//        Log.d("db", "data inserted ${chat.toId}")
//        if (result == -1.toLong()) { Toast.makeText(context, "data insertion failed", Toast.LENGTH_SHORT).show() }
//        else { Toast.makeText(context, "data inserted succesfully", Toast.LENGTH_SHORT).show() }
//    }
//
//
//}