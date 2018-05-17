package com.smartqrcodereader.ali.smartqrcodereader.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.smartqrcodereader.ali.smartqrcodereader.lib.BaseColomn
import com.smartqrcodereader.ali.smartqrcodereader.models.DataModel
import org.json.JSONObject

class DatabaseManagerHelper(context: Context) : DatabaseHelper(context) {
    private var databaseManager: DatabaseManager = DatabaseManager(this)
    private val db = databaseManager.openDatabase()

    companion object {
        private val TAG = DatabaseManagerHelper::class.java.simpleName
        private var instance: DatabaseManagerHelper? = null

        @Synchronized
        fun getInstance(context: Context): DatabaseManagerHelper {
            if (instance == null) {
                instance = DatabaseManagerHelper(context)
            }
            return instance as DatabaseManagerHelper
        }
    }

    fun insertAccount(result: GoogleSignInResult?) {
        val cv = ContentValues()
        cv.put(BaseColomn.tableLogin[1], result?.signInAccount?.email)
        db?.insert(BaseColomn.tableLogin[0], null, cv)
    }

    @SuppressLint("Recycle")
    fun isExisted(): Boolean? {
        val selectQuery = "SELECT * FROM " + BaseColomn.tableLogin[0]
        val cursor = db?.rawQuery(selectQuery, null)
        return (cursor?.count!! > 0)
    }

    @SuppressLint("Recycle")
    fun getEmailAccount(): String? {
        val selectQuery = "SELECT * FROM " + BaseColomn.tableLogin[0]
        val cursor = db?.rawQuery(selectQuery, null)
        if (cursor?.count!! > 0) {
            cursor.moveToFirst()
            return cursor.getString(cursor.getColumnIndex(BaseColomn.tableLogin[1]))
        }
        return ""
    }

    @SuppressLint("Recycle")
    fun isEmpty(): Boolean {
        val selectQuery = "SELECT * FROM " + BaseColomn.tableAttendanceColomn[0]
        val cursor = db?.rawQuery(selectQuery, null)
        return cursor?.count!! < 1
    }

    fun getAllDataAttendance(email: String?): ArrayList<DataModel> {
        val selectQuery = ("SELECT * FROM " + BaseColomn.tableAttendanceColomn[0] + " WHERE "
                + BaseColomn.tableAttendanceColomn[2] + " = '" + email + "'")
        val cursor = db?.rawQuery(selectQuery, null)
        val listDataModel = ArrayList<DataModel>()
        if (cursor?.count!! > 0) {
            if (cursor.moveToFirst()) {
                do {
                    listDataModel.add(getDataModelByEmail(cursor))
                    listDataModel.sortByDescending { it.time }
                } while (cursor.moveToNext())
            }
        }
        return listDataModel
    }

    private fun getDataModelByEmail(cursor: Cursor): DataModel {
        val dataModel = DataModel()
        dataModel.email = cursor.getString(cursor.getColumnIndex(BaseColomn.tableAttendanceColomn[2]))
        dataModel.time = cursor.getString(cursor.getColumnIndex(BaseColomn.tableAttendanceColomn[3]))
        dataModel.location = cursor.getString(cursor.getColumnIndex(BaseColomn.tableAttendanceColomn[4]))
        dataModel.meeting = cursor.getString(cursor.getColumnIndex(BaseColomn.tableAttendanceColomn[5]))
        return dataModel
    }

    fun deleteAccount() {
        val account = getEmailAccount()
        val where = BaseColomn.tableLogin[1] + "= ?"
        val args = arrayOf(account)
        db?.delete(BaseColomn.tableLogin[0], where, args)
    }

    fun insertData(data: JSONObject) {
        val email = getEmailAccount()
        val cv = ContentValues()
        cv.put(BaseColomn.tableAttendanceColomn[2], email)
        cv.put(BaseColomn.tableAttendanceColomn[3], data.getString("time"))
        cv.put(BaseColomn.tableAttendanceColomn[4], data.getString("location"))
        cv.put(BaseColomn.tableAttendanceColomn[5], data.getString("meeting"))
        db?.insert(BaseColomn.tableAttendanceColomn[0], null, cv)
    }
}