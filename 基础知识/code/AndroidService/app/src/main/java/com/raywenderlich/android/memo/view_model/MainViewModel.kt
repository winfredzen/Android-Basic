/*
 * Copyright (c) 2021 Razeware LLC
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

package com.raywenderlich.android.memo.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raywenderlich.android.memo.R
import com.raywenderlich.android.memo.model.CardState
import com.raywenderlich.android.memo.model.IconModel
import com.raywenderlich.android.memo.model.Level
import java.util.*

class MainViewModel : ViewModel() {

    var isReceiverRegistered: Boolean = false
    var isForegroundServiceRunning: Boolean = false
        set(value) {
            field = value
            updateGridVisibility.value = Unit
        }
    var isMusicServiceBound: Boolean = false

    var elapsedTime: Int = 0
    var lastOpenedCard: IconModel? = null
    var pairsSum: Int = 0
    var pairs: Int = 0
        set(value) {
            field = value

            // reset last card
            if (value == 0) lastOpenedCard = null
        }

    val closeCards = MutableLiveData<IconModel>()
    val pairMatch = MutableLiveData<IconModel>()
    val updateGridVisibility = MutableLiveData<Unit>()
    val showSuccessDialog = MutableLiveData<Unit>()

    private val iconList = listOf(
        IconModel(R.drawable.ic_tie_fighter),
        IconModel(R.drawable.ic_stormtropper),
        IconModel(R.drawable.ic_chew),
        IconModel(R.drawable.ic_combo),
        IconModel(R.drawable.ic_bb),
        IconModel(R.drawable.ic_rd),
        IconModel(R.drawable.ic_darth_maul),
    )

    fun getNumberOfColumns(level: Level): Int =
        when (level) {
            Level.BEGINNER, Level.INTERMEDIATE -> 2
            else -> 3
        }

    fun getRandomItems(numberOfCards: Int): List<IconModel> {
        val randomItems = iconList.shuffled().take(numberOfCards)
        val duplicates = randomItems.map { it.copy(id = UUID.randomUUID()) }.toMutableList()

        return with(randomItems + duplicates) {
            shuffled()
            map { it.state = CardState.CLOSED }
            toMutableList()
        }
    }

    internal fun checkIsMatchFound(clickedItem: IconModel) {
        lastOpenedCard?.let {
            if (it.res == clickedItem.res) {
                pairMatch.value = clickedItem
                twoCardsMatched()
            } else {
                closeCards.value = clickedItem
            }
        } ?: kotlin.run {
            lastOpenedCard = clickedItem
        }
    }

    private fun twoCardsMatched() {
        // notify that one more pair has been matched
        pairs++

        // upgrade level if all cards are matched
        if (pairs == pairsSum) {
            showSuccessDialog.postValue(Unit)
        }
    }
}