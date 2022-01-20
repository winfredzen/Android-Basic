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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.raywenderlich.android.memo.model.IconModel
import com.raywenderlich.android.memo.model.Level
import com.raywenderlich.android.memo.view_model.MainViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class MainViewModelUnitTest {

  @Rule
  @JvmField
  val executorRule = InstantTaskExecutorRule()

  @Mock
  private lateinit var observer: Observer<Unit>

  @Mock
  private lateinit var iconModelObserver: Observer<IconModel>

  private lateinit var mainViewModel: MainViewModel

  @Before
  fun setUp() {
    MockitoAnnotations.openMocks(this)
    mainViewModel = MainViewModel()
  }

  @Test
  fun number_of_columns_correct() {
    val result = mainViewModel.getNumberOfColumns(Level.BEGINNER)
    assertEquals(2, result)
  }

  @Test
  fun number_of_columns_is_not_3() {
    val result = mainViewModel.getNumberOfColumns(Level.BEGINNER)
    assertNotEquals(3, result)
  }

  @Test
  fun pairs_increased() {
    mainViewModel.pairs = 3
    mainViewModel.pairsSum = 4

    val iconModel = IconModel(R.drawable.ic_play_gray)
    mainViewModel.lastOpenedCard = iconModel

    mainViewModel.checkIsMatchFound(iconModel)

    // pairs increased
    assertEquals(4, mainViewModel.pairs)
  }

  @Test
  fun pairMatch_live_data_set() {
    val iconModel = IconModel(R.drawable.ic_play_gray)
    mainViewModel.lastOpenedCard = iconModel

    mainViewModel.checkIsMatchFound(iconModel)

    // pairMatch live data called
    mainViewModel.pairMatch.observeForever(iconModelObserver)
    verify(iconModelObserver).onChanged(iconModel)
    assertEquals(mainViewModel.pairMatch.value, iconModel)
  }

  @Test
  fun showSuccessDialog_live_data_called() {
    mainViewModel.pairs = 3
    mainViewModel.pairsSum = 4

    val iconModel = IconModel(R.drawable.ic_play_gray)
    mainViewModel.lastOpenedCard = iconModel
    mainViewModel.checkIsMatchFound(iconModel)

    mainViewModel.showSuccessDialog.observeForever(observer)
    verify(observer).onChanged(Unit)
    assertEquals(mainViewModel.showSuccessDialog.value, Unit)
  }

  @Test
  fun lastOpenedCard_set() {
    mainViewModel.lastOpenedCard = null

    val iconModel = IconModel(R.drawable.ic_play_gray)
    mainViewModel.checkIsMatchFound(iconModel)
    assertEquals(iconModel, mainViewModel.lastOpenedCard)
  }

  @Test
  fun closeCards_live_data_called() {
    val iconModel = IconModel(R.drawable.ic_play_gray)
    mainViewModel.lastOpenedCard = IconModel(R.drawable.ic_pause_gray)

    mainViewModel.checkIsMatchFound(iconModel)

    mainViewModel.closeCards.observeForever(iconModelObserver)
    verify(iconModelObserver).onChanged(iconModel)
    assertEquals(mainViewModel.closeCards.value, iconModel)
  }

  @Test
  fun random_items_are_duplicated() {
    val result = mainViewModel.getRandomItems(3)
    assertEquals(6, result.size)
  }
}
