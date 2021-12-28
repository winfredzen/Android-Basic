/*
 *
 *  * Copyright (c) 2018 Razeware LLC
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in
 *  * all copies or substantial portions of the Software.
 *  *
 *  * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 *  * distribute, sublicense, create a derivative work, and/or sell copies of the
 *  * Software in any work that is designed, intended, or marketed for pedagogical or
 *  * instructional purposes related to programming, coding, application development,
 *  * or information technology.  Permission for such use, copying, modification,
 *  * merger, publication, distribution, sublicensing, creation of derivative works,
 *  * or sale is expressly withheld.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  * THE SOFTWARE.
 *
 *
 */

package com.raywenderlich.android.imet.ui.list

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.raywenderlich.android.imet.R
import com.raywenderlich.android.imet.data.model.People
import kotlinx.android.synthetic.main.layout_list_item.view.*

class PeoplesListAdapter(
    private val items: List<People>,
    private val clickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  /**
   * Notifies click on an item with attached view
   */
  interface OnItemClickListener {
    fun onItemClick(people: People, itemView: View)
  }

  /**
   * Creates view for each item in the list
   */
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.layout_list_item, parent, false)
    return ViewHolder(view)
  }

  /**
   * Binds view with item info
   */
  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    (holder as ViewHolder).bind(items[position], clickListener)
  }

  /**
   * Returns the size to item list
   */
  override fun getItemCount(): Int {
    return items.size
  }

  /**
   * View for item, sets item info and click events
   */
  class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(people: People, listener: OnItemClickListener) = with(itemView) {
      textViewName.text = people.name
      textViewMet.text = people.metAt
      buttonContact.text = people.contact
      buttonContact.setOnClickListener {
        // Dial contact number
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:${people.contact}")
        itemView.context.startActivity(dialIntent)
      }

      // RecyclerView on item click
      setOnClickListener {
        listener.onItemClick(people, it)
      }
    }

  }

}
