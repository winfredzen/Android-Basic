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

import androidx.lifecycle.ViewModel
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.subjects.BehaviorSubject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class SharedViewModel : ViewModel() {

    // 处理订阅的销毁相关操作
    private val subscriptions = CompositeDisposable()

    // imagesSubject发射MutableList<Photo>类型的值，表示选择的photos，其默认值为空list
    private val imagesSubject: BehaviorSubject<MutableList<Photo>> = BehaviorSubject.createDefault(
        mutableListOf()
    )

    // selectedPhotos
    private val selectedPhotos = MutableLiveData<List<Photo>>()

    // 用来通知MainActivity更新缩略图
    private val thumbnailStatus = MutableLiveData<ThumbnailStatus>()

    init {
        // 订阅imagesSubject
        imagesSubject.subscribe { photos ->
            // LiveData的更新
            selectedPhotos.value = photos
        }.addTo(subscriptions)
    }

    // ViewModels onCleared() method is a great place to dispose of any disposables you may have lying around
    override fun onCleared() {
        subscriptions.clear()
        super.onCleared()
    }

    fun addPhoto(photo: Photo) {
        // Subject数据变化，并调用onNext
        imagesSubject.value?.add(photo)
        imagesSubject.onNext(imagesSubject.value!!)
    }

    fun clearPhotos() {
        imagesSubject.value.clear()
        imagesSubject.onNext(imagesSubject.value)
    }

    fun getSelectedPhotos(): LiveData<List<Photo>> {
        return selectedPhotos
    }

    fun getThumbnailStatus(): LiveData<ThumbnailStatus> {
        return thumbnailStatus
    }


    fun subscribeSelectedPhotos(fragment: PhotosBottomDialogFragment) {
        // 使用share，不会每次都创建一个新的Observable
        val newPhotos = fragment.selectedPhotos.share()
        subscriptions.add(newPhotos
            .doOnComplete {
                Log.v("SharedViewModel", "Completed selecting photos")
            }
            .subscribe { photo ->
                imagesSubject.value?.add(photo)
                imagesSubject.onNext(imagesSubject.value ?: mutableListOf())
            }
        )

        subscriptions.add(newPhotos
            .ignoreElements() // 只允许 complete or error 事件
            .subscribe {
                thumbnailStatus.postValue(ThumbnailStatus.READY)
            }
        )
    }

//    fun subscribeSelectedPhotos(selectedPhotos: Observable<Photo>) {
//        selectedPhotos
//            .doOnComplete {
//                Log.v("SharedViewModel", "Completed selecting photos")
//            }
//            .subscribe { photo ->
//                imagesSubject.value?.add(photo)
//                imagesSubject.onNext(imagesSubject.value!!)
//            }
//            .addTo(disposables)
//    }

    fun saveBitmapFromImageView(imageView: ImageView, context: Context): Single<String> {

        return Single.create { emitter ->
            println("saveBitmapFromImageView Thread started: ${Thread. currentThread(). name}")
            val tmpImg = "${System.currentTimeMillis()}.png"

            val os: OutputStream?

            val collagesDirectory = File(context.getExternalFilesDir(null), "collages")
            if (!collagesDirectory.exists()) {
                collagesDirectory.mkdirs()
            }

            val file = File(collagesDirectory, tmpImg)

            try {
                os = FileOutputStream(file)
                val bitmap = (imageView.drawable as BitmapDrawable).bitmap
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
                os.flush()
                os.close()

                // emit创建图片的名称
                emitter.onSuccess(tmpImg)

            } catch (e: IOException) {
                Log.e("MainActivity", "Problem saving collage", e)

                // 发生错误，emit错误
                emitter.onError(e)
            }
        }

//    fun saveBitmapFromImageView(imageView: ImageView, context: Context): Observable<String> {
//
//        return Observable.create { emitter ->
//            println("saveBitmapFromImageView Thread started: ${Thread. currentThread(). name}")
//            val tmpImg = "${System.currentTimeMillis()}.png"
//
//            val os: OutputStream?
//
//            val collagesDirectory = File(context.getExternalFilesDir(null), "collages")
//            if (!collagesDirectory.exists()) {
//                collagesDirectory.mkdirs()
//            }
//
//            val file = File(collagesDirectory, tmpImg)
//
//            try {
//                os = FileOutputStream(file)
//                val bitmap = (imageView.drawable as BitmapDrawable).bitmap
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
//                os.flush()
//                os.close()
//
//                // emit创建图片的名称
//                emitter.onNext(tmpImg)
//                emitter.onComplete()
//
//            } catch (e: IOException) {
//                Log.e("MainActivity", "Problem saving collage", e)
//
//                // 发生错误，emit错误
//                emitter.onError(e)
//            }
//        }


    }
}