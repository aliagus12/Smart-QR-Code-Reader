package com.smartqrcodereader.ali.smartqrcodereader.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.smartqrcodereader.ali.smartqrcodereader.lib.BaseColomn

open class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "attendace.db"
        const val DATABASE_VERSION = 5
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(BaseColomn.CREATE_TABLE_ATTENDANCE)
        db?.execSQL(BaseColomn.CREATE_TABLE_LOGIN)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + BaseColomn.TABLE_ATTENDANCE)
        db?.execSQL("DROP TABLE IF EXISTS " + BaseColomn.TABLE_LOGIN)
        onCreate(db)
    }
}