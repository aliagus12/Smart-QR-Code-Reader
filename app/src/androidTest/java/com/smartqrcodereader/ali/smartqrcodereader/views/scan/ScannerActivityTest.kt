package com.smartqrcodereader.ali.smartqrcodereader.views.scan

import android.content.Context
import android.content.SharedPreferences
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.smartqrcodereader.ali.smartqrcodereader.R
import com.smartqrcodereader.ali.smartqrcodereader.database.DatabaseManagerHelper
import com.smartqrcodereader.ali.smartqrcodereader.views.info.InfoActivity
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations


@RunWith(AndroidJUnit4::class)
class ScannerActivityTest {

    lateinit var databaseManagerHelper: DatabaseManagerHelper
    private lateinit var sharedPreferences: SharedPreferences
    private val TAG = ScannerActivityTest::class.java.simpleName

    @get:Rule
    private val testRule = ActivityTestRule(InfoActivity::class.java, false, false)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val context = InstrumentationRegistry.getTargetContext()
        databaseManagerHelper = DatabaseManagerHelper.getInstance(context)
        sharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE)
    }

    @After
    fun tearDown() {
        sharedPreferences.all.clear()
    }

    @Test
    fun shouldNotUrl() {
        testRule.launchActivity(null)
        onView(allOf(withId(R.id.btn_scan), isDisplayed())).perform(click())
        onView(allOf(withId(R.id.zXingScannerView))).check(matches(isDisplayed()))
        getSleep(5000)
        assertThat(sharedPreferences.getBoolean("isUrl", false), `is`(false))
    }

    @Test
    fun shouldUrl() {
        testRule.launchActivity(null)
        onView(allOf(withId(R.id.btn_scan), isDisplayed())).perform(click())
        onView(allOf(withId(R.id.zXingScannerView))).check(matches(isDisplayed()))
        getSleep(5000)
        assertThat(sharedPreferences.getBoolean("isUrl", false), `is`(true))
    }

    @Test
    fun shouldSuccess() {
        testRule.launchActivity(null)
        onView(allOf(withId(R.id.btn_scan), isDisplayed())).perform(click())
        onView(allOf(withId(R.id.zXingScannerView))).check(matches(isDisplayed()))
        getSleep(5000)
        assertThat(sharedPreferences.getInt("responseCode", 0), `is`(200))
    }

    @Test
    fun shouldFailed() {
        testRule.launchActivity(null)
        onView(allOf(withId(R.id.btn_scan), isDisplayed())).perform(click())
        onView(allOf(withId(R.id.zXingScannerView))).check(matches(isDisplayed()))
        getSleep(5000)
        assertThat(sharedPreferences.getInt("responseCode", 0), not(200))
    }

    @Test
    fun shouldResponseSuccess() {
        testRule.launchActivity(null)
        onView(allOf(withId(R.id.btn_scan), isDisplayed())).perform(click())
        onView(allOf(withId(R.id.zXingScannerView))).check(matches(isDisplayed()))
        getSleep(5000)
        assertThat(sharedPreferences.getBoolean("isResponseSuccess", false), `is`(true))
    }

    @Test
    fun shouldResponseFailed() {
        testRule.launchActivity(null)
        onView(allOf(withId(R.id.btn_scan), isDisplayed())).perform(click())
        onView(allOf(withId(R.id.zXingScannerView))).check(matches(isDisplayed()))
        getSleep(5000)
        assertThat(sharedPreferences.getBoolean("isResponseSuccess", false), `is`(false))
    }

    private fun getSleep(time: Int) {
        try {
            Thread.sleep(time.toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}