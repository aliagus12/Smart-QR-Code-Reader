package com.smartqrcodereader.ali.smartqrcodereader.views.info

import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.smartqrcodereader.ali.smartqrcodereader.database.DatabaseManagerHelper
import com.smartqrcodereader.ali.smartqrcodereader.views.info.InfoActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class InfoActivityTest {

    lateinit var databaseManagerHelper: DatabaseManagerHelper

    @get:Rule
    private val testRule = ActivityTestRule(InfoActivity::class.java, false, false)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val context = InstrumentationRegistry.getTargetContext()
        databaseManagerHelper = DatabaseManagerHelper.getInstance(context)
    }

    @Test
    fun shouldDeleteAccountFromDatabase() {
    }
}