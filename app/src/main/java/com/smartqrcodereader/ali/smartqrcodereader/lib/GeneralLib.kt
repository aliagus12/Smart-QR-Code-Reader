package com.smartqrcodereader.ali.smartqrcodereader.lib

import android.content.Context
import android.provider.Settings
import java.text.SimpleDateFormat
import java.util.*

class GeneralLib {
    companion object {
        fun getDeviceDateFormat(context: Context?): String {
            val format = Settings.System.getString(context?.contentResolver, Settings.System.DATE_FORMAT)
            return format ?: "MMMM dd, yyyy"
        }

        fun getDeviceTimeFormat(context: Context?): String {
            val format = Settings.System.getString(context?.contentResolver, Settings.System.TIME_12_24)
            return if (format != null && format == "12") "h:mm a" else "k:mm"
        }

        fun formatTimeMillis(format: String?, timeMillis: Long?): String {
            val date = Date(timeMillis ?: 0)
            val dateFormatter = SimpleDateFormat(format, Locale.US)
            return dateFormatter.format(date)
        }
    }
}