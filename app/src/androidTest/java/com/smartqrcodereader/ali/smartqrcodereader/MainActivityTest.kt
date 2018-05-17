package com.smartqrcodereader.ali.smartqrcodereader

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.smartqrcodereader.ali.smartqrcodereader.database.DatabaseManagerHelper
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var databaseManagerHelper: DatabaseManagerHelper

    @get:Rule
    private val testRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        val context = InstrumentationRegistry.getTargetContext()
        databaseManagerHelper = DatabaseManagerHelper.getInstance(context)
    }

    @After
    fun tearDown() {
        testRule.finishActivity()
    }

    @Test
    fun shouldToLogin() {
        testRule.launchActivity(null)
        assertThat(databaseManagerHelper.isExisted(), `is`(true))
        onView(allOf(withId(R.id.btn_scan))).check(matches(isDisplayed()))
    }

    @Test
    fun shouldToInfo() {
        testRule.launchActivity(null)
        assertThat(databaseManagerHelper.isExisted(), `is`(false))
        onView(allOf(withId(R.id.sign_in_button))).check(matches(isDisplayed()))
    }
}