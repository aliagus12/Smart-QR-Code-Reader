package com.smartqrcodereader.ali.smartqrcodereader.views.login

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.smartqrcodereader.ali.smartqrcodereader.database.DatabaseManagerHelper

class LoginPresenter(
        context: Context,
        private val view: LoginContract.View
): LoginContract.Presenter {

    private val databaseManagerHelper = DatabaseManagerHelper.getInstance(context)

    override fun saveAccount(result: GoogleSignInResult?) {
        databaseManagerHelper.insertAccount(result)
    }
}