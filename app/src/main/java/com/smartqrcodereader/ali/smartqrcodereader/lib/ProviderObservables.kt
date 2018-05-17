package com.smartqrcodereader.ali.smartqrcodereader.lib

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.util.Log
import com.smartqrcodereader.ali.smartqrcodereader.database.DatabaseManagerHelper
import com.smartqrcodereader.ali.smartqrcodereader.models.DataModel
import io.reactivex.Observable
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class ProviderObservables(context: Context) {

    val TAG = ProviderObservables::class.java.simpleName!!
    private val databaseManagerHelper = DatabaseManagerHelper.getInstance(context)
    private val sharedPreferences = context.getSharedPreferences("pref", MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    fun getObservableAllData(
            databaseManagerHelper: DatabaseManagerHelper,
            email: String?
    ): Observable<ArrayList<DataModel>> {
        return Observable.create { emitter ->
            val listDataAll = databaseManagerHelper.getAllDataAttendance(email)
            emitter.onNext(listDataAll)
            emitter.onComplete()
        }
    }

    fun getObservableRequest(url: String?): Observable<JSONObject> {
        return Observable.create { emitter ->
            val id = databaseManagerHelper.getEmailAccount()
            val requestBody = FormBody.Builder()
                    .add("ID", id?:"")
                    .build()
            val request = Request.Builder()
                    .url(url?:"")
                    .addHeader("Accept", "application/json")
                    .addHeader("content-Type", "application/json")
                    .post(requestBody)
                    .build()
            val client = OkHttpClient.Builder()
                    .build()
            client.newCall(request)
                    .enqueue(object : Callback {
                        override fun onFailure(call: Call?, e: IOException?) {
                            e?.let { emitter.tryOnError(it) }
                        }

                        override fun onResponse(call: Call?, response: Response?) {
                            val responseString = response?.body()?.string()
                            Log.d(TAG, "response $responseString")
                            editor.putString("responseString", responseString)
                            editor.apply()
                            if (response?.code() ?: "" == 200) {
                                response?.code()?.let { editor.putInt("responseCode", it) }
                                editor.apply()
                                responseString?.let {
                                    if (
                                            !it.contains("<!DOCTYPE html>") &&
                                            it.contains("\"success\":true") &&
                                            it.contains("time") &&
                                            it.contains("location") &&
                                            it.contains("meeting")
                                    ) {
                                        editor.putBoolean("isResponseSuccess", true)
                                        editor.apply()
                                        val jsonObjectResponse = JSONObject(responseString)
                                        emitter.onNext(jsonObjectResponse)
                                        emitter.onComplete()
                                    } else {
                                        editor.putBoolean("isResponseSuccess", false)
                                        editor.apply()
                                        emitter.onError(Exception())
                                    }
                                }
                            } else {
                                editor.putBoolean("isResponseSuccess", false)
                                response?.code()?.let { editor.putInt("responseCode", it) }
                                editor.apply()
                                emitter.onError(Exception())
                            }
                        }
                    })
        }
    }
}