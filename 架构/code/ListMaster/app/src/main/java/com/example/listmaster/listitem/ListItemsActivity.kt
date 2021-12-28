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

package com.example.listmaster.listitem

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.listmaster.R
import com.example.listmaster.databinding.ActivityListItemsBinding
import com.example.listmaster.databinding.ContentListItemsBinding
import com.example.listmaster.databinding.DialogAddItemBinding
import com.example.listmaster.listcategory.ListCategory
import com.example.listmaster.listcategory.ListCategoryViewModel

class ListItemsActivity : AppCompatActivity() {

  companion object {
    val LIST_CATEGORY_ID = "LIST_CATEGORY_ID"
    val CATEGORY_NAME = "CATEGORY_NAME"
  }

  private lateinit var activityListItemsBinding: ActivityListItemsBinding
  private lateinit var contentListItemsBinding: ContentListItemsBinding
  private lateinit var listItemAdapter: ListItemAdapter
  private lateinit var listCategory: ListCategory

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    activityListItemsBinding = DataBindingUtil.setContentView(this,
        R.layout.activity_list_items)


    val listCategoryViewModel = ListCategoryViewModel(listCategory)
    val listItemsHeader = activityListItemsBinding.listItemsHeaderInclude!!
    listItemsHeader.listCategoryItem = listCategoryViewModel
    // call our function to set up the add fab button logic
    setupAddButton()

  }

  /**
   * sets up the Fab button to launch a dialog allowing the user to add an item to their list.
   */
  private fun setupAddButton() {
    activityListItemsBinding.fab.setOnClickListener {

      // Setup the dialog
      val alertDialogBuilder = AlertDialog.Builder(this)
      alertDialogBuilder.setTitle("Title")
      val dialogAddItemBinding = DialogAddItemBinding.inflate(layoutInflater)

      alertDialogBuilder.setView(dialogAddItemBinding.root)

      // Setup the positive and negative buttons. When the user clicks ok, a record is added to the
      // db, the db is queried and the RecyclerView is updated.
      alertDialogBuilder.setPositiveButton(android.R.string.ok, { dialog: DialogInterface, which: Int ->
      })
      alertDialogBuilder.setNegativeButton(android.R.string.cancel, { dialog: DialogInterface, which: Int -> })
      alertDialogBuilder.show()
    }
  }
}
