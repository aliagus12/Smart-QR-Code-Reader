package com.smartqrcodereader.ali.smartqrcodereader.views.scan

import android.content.Context
import android.util.Log
import com.smartqrcodereader.ali.smartqrcodereader.database.DatabaseManagerHelper
import com.smartqrcodereader.ali.smartqrcodereader.lib.ProviderObservables
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ScannerPresenter(
        context: Context,
        private val view: ScannerContract.View
) : ScannerContract.Presenter {

    private val databaseManagerHelper = DatabaseManagerHelper.getInstance(context)
    private val providerObservable = ProviderObservables(context)
    private val TAG = ScannerPresenter::class.java.simpleName


    override fun sendRequest(url: String?) {
        val observable = providerObservable.getObservableRequest(url)
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.d(TAG, "onNext")
                    databaseManagerHelper.insertData(it)
                    view.onSuccessSendRequest()
                }, {
                    Log.d(TAG, "onError")
                    view.onRequestError()
                })
    }
}