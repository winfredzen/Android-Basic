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

package com.raywenderlich.android.memo.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.raywenderlich.android.memo.R
import com.raywenderlich.android.memo.model.CardState
import com.raywenderlich.android.memo.model.IconModel

class IconAdapter(
  private val context: Context,
  private val checkIsMatchFound: (clickedItem: IconModel) -> Unit
) : BaseAdapter() {

  private var icons: MutableList<IconModel> = mutableListOf()

  override fun getCount(): Int = icons.size

  override fun getItemId(position: Int): Long = icons[position].id.leastSignificantBits

  override fun getItem(position: Int): IconModel = icons[position]

  override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
    val view = convertView ?: LayoutInflater.from(context).inflate(
      R.layout.layout_icon,
      parent,
      false
    )
    val item = icons[position]

    view.findViewById<ImageView>(R.id.ivIcon)?.apply {
      this.background = flipCard(item.res, item.state)

      setOnClickListener {
        if (item.state == CardState.CLOSED) {
          item.state = CardState.OPEN
          this.background = flipCard(item.res, item.state)
          checkIsMatchFound(item)
        }
      }
    }
    return view
  }

  fun updateData(randomStarWarsIcons: List<IconModel>) {
    icons.run {
      clear()
      addAll(randomStarWarsIcons)
    }
    notifyDataSetChanged()
  }

  fun pairMatch(lastOpenedCard: IconModel?, clickedItem: IconModel?) {
    icons.run {
      find { it.id == lastOpenedCard?.id }?.state = CardState.PAIRED
      find { it.id == clickedItem?.id }?.state = CardState.PAIRED
    }
    notifyDataSetInvalidated()
  }

  fun revertVisibility(lastOpenedCard: IconModel?, clickedItem: IconModel?) {
    icons.run {
      find { it.id == lastOpenedCard?.id }?.state = CardState.CLOSED
      find { it.id == clickedItem?.id }?.state = CardState.CLOSED
    }
    notifyDataSetInvalidated()
  }

  private fun flipCard(@DrawableRes res: Int, state: CardState): Drawable? {
    val src = when (state) {
      CardState.OPEN -> res
      CardState.CLOSED -> R.drawable.ic_card_background
      CardState.PAIRED -> android.R.color.transparent
    }
    return ContextCompat.getDrawable(context, src)
  }
}