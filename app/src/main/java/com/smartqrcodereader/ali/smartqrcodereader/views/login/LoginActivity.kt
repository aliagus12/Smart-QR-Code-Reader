package com.smartqrcodereader.ali.smartqrcodereader.views.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.smartqrcodereader.ali.smartqrcodereader.R
import com.smartqrcodereader.ali.smartqrcodereader.views.info.InfoActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.intentFor

class LoginActivity : AppCompatActivity(), LoginContract.View, View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private val loginPresenter: LoginPresenter by lazy {
        LoginPresenter(this, this@LoginActivity)
    }

    companion object {
        const val SIGN_IN = 2001
    }
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleApiClient
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private lateinit var progressBar: ProgressBar
    private val TAG = LoginActivity::class.java.simpleName
    private var result: GoogleSignInResult? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        onGoogleApiClient()
        onLogOut()
        sign_in_button.setSize(SignInButton.SIZE_STANDARD)
        sign_in_button.setOnClickListener(this)
    }

    override fun onGoogleApiClient() {
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .build()
        googleSignInClient.connect()
    }

    override fun onLogOut() {
        mAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            user?.let {
                mAuth.signOut()
            }
        }
    }

    override fun onClick(view: View?) {
        view?.id.let {
            when (it) {
                R.id.sign_in_button -> {
                    var signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleSignInClient)
                    startActivityForResult(signInIntent, SIGN_IN)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SIGN_IN) {
            progressBar = ProgressBar(this@LoginActivity, null, android.R.attr.progressBarStyleLarge)
            result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            val params = RelativeLayout.LayoutParams(150, 150)
            params.addRule(RelativeLayout.CENTER_IN_PARENT)
            login_container.addView(progressBar, params)
            progressBar.visibility = View.VISIBLE
            if (result?.isSuccess!!) {
                loginPresenter.saveAccount(result)
                startActivity(intentFor<InfoActivity>())
                finish()
            } else {
                Log.d(TAG, "login cancel")
                snackbar(login_container, R.string.login_failed)
            }
            progressBar.visibility = View.GONE
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onConnected(bundle: Bundle?) {
        if (!googleSignInClient.isConnected) {
            googleSignInClient.connect()
        } else {
            Auth.GoogleSignInApi.revokeAccess(googleSignInClient)
        }
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.d(TAG, "connection Suspend")
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        snackbar(login_container, R.string.playservice_error)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }
}
