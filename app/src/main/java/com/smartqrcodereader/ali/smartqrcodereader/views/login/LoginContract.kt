package com.smartqrcodereader.ali.smartqrcodereader.views.login

import com.google.android.gms.auth.api.signin.GoogleSignInResult

interface LoginContract {

    interface View {
        fun onGoogleApiClient()
        fun onLogOut()
    }

    interface Presenter {
        fun saveAccount(result: GoogleSignInResult?)
    }
}