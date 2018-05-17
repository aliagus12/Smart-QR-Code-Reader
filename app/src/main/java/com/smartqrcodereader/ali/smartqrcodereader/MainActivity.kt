package com.smartqrcodereader.ali.smartqrcodereader

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.smartqrcodereader.ali.smartqrcodereader.database.DatabaseManagerHelper
import com.smartqrcodereader.ali.smartqrcodereader.views.info.InfoActivity
import com.smartqrcodereader.ali.smartqrcodereader.views.login.LoginActivity
import org.jetbrains.anko.intentFor

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        checkExisting()
    }

    private fun checkExisting() {
        var databaseManagerHelper = DatabaseManagerHelper.getInstance(applicationContext)
        if (databaseManagerHelper.isExisted()!!) {
            gotoInfo()
        } else {
            gotoLogin()
        }
    }

    private fun gotoInfo() {
        startActivity(intentFor<InfoActivity>())
    }

    private fun gotoLogin() {
        startActivity(intentFor<LoginActivity>())
        finish()
    }
}
