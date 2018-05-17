package com.smartqrcodereader.ali.smartqrcodereader.views.scan

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import com.smartqrcodereader.ali.smartqrcodereader.R
import kotlinx.android.synthetic.main.activity_scanner.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import org.jetbrains.anko.design.indefiniteSnackbar
import org.jetbrains.anko.indeterminateProgressDialog

class ScannerActivity : AppCompatActivity(), ScannerContract.View, ZXingScannerView.ResultHandler {

    private val scannerPresenter: ScannerPresenter by lazy {
        ScannerPresenter(this, this)
    }

    private var pDialog: ProgressDialog? = null

    companion object {
        const val RESPONSE = "response"
        const val REQUEST = "request"
        const val CAMERA_ID = -1
    }

    private val formats = arrayListOf(BarcodeFormat.QR_CODE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner)
        zXingScannerView
        zXingScannerView.setFormats(formats)
        pDialog = indeterminateProgressDialog("Please wait...")
        pDialog?.dismiss()
    }
    override fun onResume() {
        zXingScannerView.setResultHandler(this)
        zXingScannerView.startCamera(CAMERA_ID)
        zXingScannerView.setAutoFocus(true)
        super.onResume()

    }

    override fun handleResult(result: Result?) {
        val editor = getSharedPreferences("pref", Context.MODE_PRIVATE).edit()
        val uriNotification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val ringtone = RingtoneManager.getRingtone(this@ScannerActivity, uriNotification)
        ringtone.play()
        if (result?.text?.length ?: 0 > 8) {
            val urlResultSecure = result?.text?.substring(0, 8)
            val urlResult = result?.text?.substring(0, 7)
            val isUrl = urlResultSecure.equals("https://") || urlResult.equals("http://")
            if (isUrl) {
                pDialog?.show()
                editor.putBoolean("isUrl", true).apply()
                scannerPresenter.sendRequest(result?.text)
            } else {
                editor.putBoolean("isUrl", false).apply()
                showInvalidQrCode(result?.text)
            }
        } else {
            showInvalidQrCode(result?.text)
        }
    }

    override fun showInvalidQrCode(text: String?) {
        indefiniteSnackbar(zXingScannerView, R.string.not_url, R.string.try_again) { onResume() }
    }

    override fun onSuccessSendRequest() {
        pDialog?.dismiss()
        val intent = Intent()
        intent.putExtra(RESPONSE, "success")
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onPause() {
        super.onPause()
        zXingScannerView.stopCamera()
    }

    override fun onRequestError() {
        indefiniteSnackbar(zXingScannerView, R.string.error, R.string.try_again) { onResume() }
        pDialog?.dismiss()
    }
}
