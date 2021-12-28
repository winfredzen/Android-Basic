/*
 * Copyright (c) 2018 Razeware LLC
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
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.example.listmaster.listcategory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.listmaster.databinding.HolderListCategoryItemBinding

data class ListCategoryAdapter(var categoryList: List<ListCategory>, private val listCategoriesActivity: ListCategoriesActivity) : RecyclerView.Adapter<ListCategoryViewHolder>() {

  override fun getItemCount(): Int {
    return categoryList.size
  }

  override fun onBindViewHolder(holder: ListCategoryViewHolder, position: Int) {
    holder.setListCategoryItem(categoryList[position])
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListCategoryViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val holderListCategoryItemBinding = HolderListCategoryItemBinding.inflate(layoutInflater, parent, false)
    return ListCategoryViewHolder(holderListCategoryItemBinding, listCategoriesActivity)
  }
}