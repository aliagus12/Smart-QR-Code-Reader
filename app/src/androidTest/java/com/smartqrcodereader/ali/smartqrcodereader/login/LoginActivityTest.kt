package com.smartqrcodereader.ali.smartqrcodereader.login

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.smartqrcodereader.ali.smartqrcodereader.R
import com.smartqrcodereader.ali.smartqrcodereader.database.DatabaseManagerHelper
import com.smartqrcodereader.ali.smartqrcodereader.views.login.LoginActivity
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations


@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    companion object {
        const val TIMER_SLEEP = 3000
    }
    private lateinit var databaseManagerHelper: DatabaseManagerHelper
    val TAG = LoginActivityTest::class.java.simpleName!!

    @get:Rule
    private val testRule = ActivityTestRule(LoginActivity::class.java, false, false)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val context = InstrumentationRegistry.getTargetContext()
        databaseManagerHelper = DatabaseManagerHelper.getInstance(context)
    }

    @Test
    fun shouldEmailInsertToDatabase() {
        testRule.launchActivity(null)
        onView(allOf(withId(R.id.sign_in_button), isDisplayed())).perform(click())
        onView(allOf(withId(R.id.btn_scan))).check(matches(isDisplayed()))
        val emailAccount = databaseManagerHelper.getEmailAccount()
        val emailExpected = "aliagushutapea1@gmail.com"
        assertThat(emailAccount, `is`(emailExpected))
    }

    @Test
    fun shouldDeleteAccountFromDatabase() {
        testRule.launchActivity(null)
        onView(allOf(withId(R.id.sign_in_button), isDisplayed())).perform(click())
        onView(allOf(withId(R.id.btn_scan))).check(matches(isDisplayed()))
        val emailAccount = databaseManagerHelper.getEmailAccount()
        val emailExpected = "aliagushutapea1@gmail.com"
        assertThat(emailAccount, `is`(emailExpected))
        onView(allOf(withId(R.id.menu_exit), isDisplayed())).perform(click())
        onView(allOf(withId(android.R.id.button1), ViewMatchers.withText("OK"), isDisplayed())).perform(click())
        assertThat(databaseManagerHelper.isExisted(), `is`(false))
    }

    private fun getSleep() {
        try {
            Thread.sleep(TIMER_SLEEP.toLong())
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }
}