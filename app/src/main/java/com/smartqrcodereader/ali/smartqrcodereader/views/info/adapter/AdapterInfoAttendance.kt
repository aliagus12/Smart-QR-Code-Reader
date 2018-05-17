package com.smartqrcodereader.ali.smartqrcodereader.views.info.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smartqrcodereader.ali.smartqrcodereader.R
import com.smartqrcodereader.ali.smartqrcodereader.database.DatabaseManagerHelper
import com.smartqrcodereader.ali.smartqrcodereader.lib.GeneralLib
import com.smartqrcodereader.ali.smartqrcodereader.models.DataModel
import kotlinx.android.synthetic.main.adapter_data.view.*

class AdapterInfoAttendance(
        val context: Context?,
        private var dataList: MutableList<DataModel>?,
        private var viewType: MutableList<Int>?
) : RecyclerView.Adapter<AdapterInfoAttendance.ViewHolder>() {

    private var viewHolderContent: ViewHolderContent? = null
    private var databaseManagerHelper = context?.let { DatabaseManagerHelper.getInstance(it) }

    companion object {
        const val VIEW_DATA = 1
        const val VIEW_LOADING = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        return when (viewType) {
            VIEW_DATA -> ViewHolderContent(
                    layoutInflater.inflate(R.layout.adapter_data, null)
            )

            else -> ViewHolderLoading(
                    layoutInflater.inflate(R.layout.loading_data, null)
            )
        }
    }

    override fun getItemCount(): Int {
        return dataList!!.size
    }

    override fun getItemViewType(position: Int): Int {
        return viewType?.get(position)!!
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            VIEW_DATA -> {
                viewHolderContent = holder as ViewHolderContent
                val dataModel = dataList?.get(position)
                viewHolderContent!!.bind(dataModel)
            }

            VIEW_LOADING -> {
                val viewHolderLoading = holder as ViewHolderLoading
            }
        }
    }

    open inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    inner class ViewHolderContent(itemView: View?) : ViewHolder(itemView) {
        fun bind(dataModel: DataModel?) {
            val location = (dataModel?.location?.substring(0, 1)?.toUpperCase() ?: "") + dataModel?.location?.substring(1)
            itemView.txt_location.text = location
            val lastModifiedData = (dataModel?.time ?: "0").toLong()
            val timeFormat = GeneralLib.getDeviceTimeFormat(context)
            val dateFormat = GeneralLib.getDeviceDateFormat(context)
            val dateNow = GeneralLib.formatTimeMillis(dateFormat, System.currentTimeMillis())
            val dateData = GeneralLib.formatTimeMillis(dateFormat, lastModifiedData)
            val timeData = GeneralLib.formatTimeMillis(timeFormat, lastModifiedData)
            if (dateNow == dateData) {
                itemView.txt_time.text = timeData
            } else {
                itemView.txt_time.text = dateData
            }
        }
    }

    inner class ViewHolderLoading(itemView: View?) : ViewHolder(itemView)
}
