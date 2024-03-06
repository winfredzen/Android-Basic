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
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.combinestagram

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlinx.android.synthetic.main.layout_photo_bottom_sheet.*


class PhotosBottomDialogFragment : BottomSheetDialogFragment(), PhotosAdapter.PhotoListener {

    // 内部使用
    private val selectedPhotosSubject = PublishSubject.create<Photo>()

    // 向外暴露的Observable
    val selectedPhotos: Observable<Photo>
        get() = selectedPhotosSubject.hide()

    private lateinit var viewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_photo_bottom_sheet, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val ctx = activity
        ctx?.let {
            viewModel = ViewModelProvider(ctx).get(SharedViewModel::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photosRecyclerView.layoutManager = GridLayoutManager(context, 3)
        photosRecyclerView.adapter = PhotosAdapter(PhotoStore.photos, this)
    }

    // 选择某个图片后
    override fun photoClicked(photo: Photo) {
        // 发射
        selectedPhotosSubject.onNext(photo)
    }

    // Dialog销毁
    override fun onDestroyView() {
        // 完成
        selectedPhotosSubject.onComplete()
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): PhotosBottomDialogFragment {
            return PhotosBottomDialogFragment()
        }
    }
}