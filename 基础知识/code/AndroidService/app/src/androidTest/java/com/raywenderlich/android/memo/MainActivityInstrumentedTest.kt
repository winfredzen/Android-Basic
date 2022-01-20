/*
 * Copyright (c) 2020 Razeware LLC
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 * 
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.memo

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.raywenderlich.android.memo.ui.MainActivity
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

  @Rule
  @JvmField
  var activityScenarioRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(
      MainActivity::class.java)

  private var decorView: View? = null

  @Before
  fun setUp() {
    activityScenarioRule.scenario.onActivity {
      decorView = it.window.decorView
    }
  }

  @Test
  fun btn_play_enabled_and_on_click_show_cards() {
    onView(withId(R.id.btnPlay))
        .check(matches(isEnabled()))
        .check(matches(isDisplayed()))
        .perform(click())

    onView(withId(R.id.gridview))
        .check(matches(isDisplayed()))
  }

  @Test
  fun btn_quit_visible_and_shows_play_button() {
    onView(withId(R.id.btnPlay))
        .check(matches(isEnabled()))
        .check(matches(isDisplayed()))

    onView(withId(R.id.btnQuit))
        .check(matches(isEnabled()))
        .check(matches(not(isDisplayed())))

    onView(withId(R.id.btnPlay)).perform(click())

    onView(withId(R.id.btnQuit))
        .check(matches(isEnabled()))
        .check(matches(isDisplayed()))
        .perform(click())

    onView(withId(R.id.btnPlay))
        .check(matches(isEnabled()))
        .check(matches(isDisplayed()))
  }

  @Test
  fun btn_play_music_enabled() {
    onView(withId(R.id.btnPlayMusic))
        .check(matches(isEnabled()))
        .check(matches(isDisplayed()))

    onView(withId(R.id.btnPauseMusic))
        .check(matches(not(isEnabled())))
        .check(matches(isDisplayed()))

    onView(withId(R.id.btnShuffleMusic))
        .check(matches(not(isEnabled())))
        .check(matches(isDisplayed()))

    onView(withId(R.id.btnStopMusic))
        .check(matches(not(isEnabled())))
        .check(matches(isDisplayed()))

    onView(withId(R.id.btnSongName))
        .check(matches(not(isDisplayed())))
  }

  @Test
  fun click_on_btn_play_music_enables_other_buttons() {
//    onView(withId(R.id.btnPlayMusic))
//        .perform(click())
//        .check(matches(not(isEnabled())))
//
//    onView(withId(R.id.btnPauseMusic))
//        .check(matches(isEnabled()))
//
//    onView(withId(R.id.btnShuffleMusic))
//        .check(matches(isEnabled()))
//
//    onView(withId(R.id.btnStopMusic))
//        .check(matches(isEnabled()))
//
//    onView(withId(R.id.btnSongName))
//        .check(matches(isEnabled()))
//        .check(matches(isDisplayed()))
  }

  @Test
  fun btn_get_song_name_has_text() {
    onView(withId(R.id.btnSongName)).check(matches(withText("GET SONG NAME")))
  }

  @Test
  fun show_toast_on_music_buttons_click() {
    onView(withId(R.id.btnPlayMusic))
        .perform(click())
        .inRoot(withDecorView(not(decorView)))
        .check(matches(isDisplayed()))

    onView(withId(R.id.btnPauseMusic))
        .perform(click())
        .inRoot(withDecorView(not(decorView)))
        .check(matches(isDisplayed()))

    onView(withId(R.id.btnShuffleMusic))
        .perform(click())
        .inRoot(withDecorView(not(decorView)))
        .check(matches(isDisplayed()))

    onView(withId(R.id.btnStopMusic))
        .perform(click())
        .inRoot(withDecorView(not(decorView)))
        .check(matches(isDisplayed()))
  }

  @Test
  fun btn_song_name_on_click_shows_toast() {
//    onView(withId(R.id.btnPlayMusic))
//        .perform(click())
//
//    onView(withId(R.id.btnSongName))
//        .perform(click())
//        .inRoot(withDecorView(not(decorView)))
//        .check(matches(isDisplayed()))
  }
}
