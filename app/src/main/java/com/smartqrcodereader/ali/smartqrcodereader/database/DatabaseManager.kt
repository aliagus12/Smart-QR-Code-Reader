package com.smartqrcodereader.ali.smartqrcodereader.database

import android.database.sqlite.SQLiteDatabase

class DatabaseManager(private val databaseHelper: DatabaseHelper){
    companion object {
        private val TAG = DatabaseManager::class.java.simpleName
    }

    private var sqLiteDatabase: SQLiteDatabase? = null

    fun openDatabase(): SQLiteDatabase? {
        if (sqLiteDatabase == null) {
            sqLiteDatabase = databaseHelper.writableDatabase
        }
        if (sqLiteDatabase?.isOpen!!) {
            sqLiteDatabase = databaseHelper.writableDatabase
        }
        return sqLiteDatabase
    }
}