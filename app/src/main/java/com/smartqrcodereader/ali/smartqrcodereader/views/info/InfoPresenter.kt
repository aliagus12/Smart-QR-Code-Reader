package com.smartqrcodereader.ali.smartqrcodereader.views.info

import android.content.Context
import com.smartqrcodereader.ali.smartqrcodereader.database.DatabaseManagerHelper
import com.smartqrcodereader.ali.smartqrcodereader.lib.ProviderObservables
import com.smartqrcodereader.ali.smartqrcodereader.models.DataModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class InfoPresenter(
        context: Context,
        private var view: InfoContract.View
) : InfoContract.Presenter {

    private val databaseManagerHelper = DatabaseManagerHelper.getInstance(context)
    private val providerObservable = ProviderObservables(context)

    override fun checkDataLocal() {
        view.showViewData(databaseManagerHelper.isEmpty())
    }

    override fun loadData() {
        val email = databaseManagerHelper.getEmailAccount()
        val listType = ArrayList<Int>()
        providerObservable.getObservableAllData(databaseManagerHelper, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    it.forEach { listType.add(1) }
                    view.showAllData(it as MutableList<DataModel>, listType as MutableList<Int>)
                }
    }

    override fun logOut() {
        databaseManagerHelper.deleteAccount()
        view.toMainActivity()
    }
}