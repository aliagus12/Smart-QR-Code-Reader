package com.smartqrcodereader.ali.smartqrcodereader.views.info

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.CoordinatorLayout
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.smartqrcodereader.ali.smartqrcodereader.MainActivity
import com.smartqrcodereader.ali.smartqrcodereader.R
import com.smartqrcodereader.ali.smartqrcodereader.models.DataModel
import com.smartqrcodereader.ali.smartqrcodereader.views.anim.FloatingOnScroll
import com.smartqrcodereader.ali.smartqrcodereader.views.info.adapter.AdapterInfoAttendance
import com.smartqrcodereader.ali.smartqrcodereader.views.scan.ScannerActivity
import kotlinx.android.synthetic.main.activity_info.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.intentFor

class InfoActivity : AppCompatActivity(), InfoContract.View, View.OnClickListener {

    private val infoActivityPresenter: InfoPresenter by lazy {
        InfoPresenter(this, this@InfoActivity)
    }

    companion object {
        private val TAG = InfoActivity::class.java.simpleName
        const val SCAN_REQUEST = 109
        const val PERMISSION_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        val params = btn_scan.layoutParams as CoordinatorLayout.LayoutParams
        params.behavior = FloatingOnScroll()
        btn_scan.layoutParams = params
        btn_scan.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        infoActivityPresenter.checkDataLocal()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }

    override fun showViewData(isEmpty: Boolean) {
        if (isEmpty) {
            view_no_data.visibility = View.VISIBLE
        } else {
            view_no_data.visibility = View.GONE
            infoActivityPresenter.loadData()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onClick(view: View?) {
        view?.id.let {
            when (it) {
                R.id.btn_scan -> {
                    checkPermissionCamera()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun checkPermissionCamera() {
        val granted = ActivityCompat.checkSelfPermission(this@InfoActivity, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        val listPermission = arrayOf(android.Manifest.permission.CAMERA)
        if (granted) {
            startActivityForResult(intentFor<ScannerActivity>(), SCAN_REQUEST)
        } else {
            requestPermissions(listPermission, PERMISSION_CODE)
            return
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(intentFor<ScannerActivity>(), SCAN_REQUEST)
                } else {
                    snackbar(info_container, R.string.permission_denied)
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun showAllData(listDataModel: MutableList<DataModel>?, listType: MutableList<Int>) {
        val adapterInfoAttendances = AdapterInfoAttendance(applicationContext, listDataModel, listType)
        val linearLayout = LinearLayoutManager(applicationContext)
        val divider = DividerItemDecoration(applicationContext, linearLayout.orientation)
        divider.setDrawable(getDrawable(R.drawable.divider))
        recycler_data_attendance.layoutManager = linearLayout
        recycler_data_attendance.addItemDecoration(divider)
        recycler_data_attendance.adapter = adapterInfoAttendances
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_info, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.itemId.let {
            when (it) {
                R.id.menu_exit -> {
                    showDialogLogOut()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showDialogLogOut() {
        alert {
            message = "Are you sure to Exit?"
            positiveButton("Ok", { infoActivityPresenter.logOut() })
            negativeButton("No", { })
        }.show()
    }

    override fun toMainActivity() {
        startActivity(intentFor<MainActivity>())
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SCAN_REQUEST) {
            val response = data?.getStringExtra("response").toString()
            if (response == "success") {
                snackbar(btn_scan, R.string.success)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
