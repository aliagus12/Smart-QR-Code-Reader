package com.smartqrcodereader.ali.smartqrcodereader.views.scan

interface ScannerContract {
    interface View {
        fun showInvalidQrCode(text: String?)
        fun onSuccessSendRequest()
        fun onRequestError()
    }

    interface Presenter {
        fun sendRequest(url: String?)
    }
}