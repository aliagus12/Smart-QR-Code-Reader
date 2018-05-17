package com.smartqrcodereader.ali.smartqrcodereader.views.info

import com.smartqrcodereader.ali.smartqrcodereader.models.DataModel

interface InfoContract {

    interface View {
        fun showViewData(isEmpty: Boolean)
        fun showAllData(listDataModel: MutableList<DataModel>?, listType: MutableList<Int>)
        fun toMainActivity()
        fun checkPermissionCamera()
        fun showDialogLogOut()
    }

    interface Presenter {
        fun checkDataLocal()
        fun loadData()
        fun logOut()
    }
}