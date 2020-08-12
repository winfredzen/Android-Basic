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

package com.raywenderlich.android.memories.ui.images

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.raywenderlich.android.memories.App
import com.raywenderlich.android.memories.R
import com.raywenderlich.android.memories.model.Image
import com.raywenderlich.android.memories.networking.NetworkStatusChecker
import com.raywenderlich.android.memories.ui.images.dialog.ImageOptionsDialogFragment
import com.raywenderlich.android.memories.utils.gone
import com.raywenderlich.android.memories.utils.toast
import com.raywenderlich.android.memories.utils.visible
import com.raywenderlich.android.memories.worker.DownloadImageWorker
import kotlinx.android.synthetic.main.fragment_images.*

/**
 * Fetches and displays notes from the API.
 */
class ImagesFragment : Fragment(), ImageOptionsDialogFragment.ImageOptionsListener {

  private val adapter by lazy { ImageAdapter(::onItemSelected) }
  private val remoteApi = App.remoteApi
  private val networkStatusChecker by lazy {
    NetworkStatusChecker(activity?.getSystemService(ConnectivityManager::class.java))
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_images, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initListeners()
    initUi()
  }

  private fun initUi() {
    progress.visible()
    noData.visible()
    imagesRecyclerView.layoutManager = LinearLayoutManager(context)
    imagesRecyclerView.adapter = adapter
    getAllImages()
  }

  private fun initListeners() {
    pullToRefresh.setOnRefreshListener {
      getAllImages()
    }
  }

  private fun onItemSelected(taskId: String) {
    val dialog = ImageOptionsDialogFragment.newInstance(taskId)
    dialog.setImageOptionsListener(this)
    dialog.show(childFragmentManager, dialog.tag)
  }

  override fun onImageDownload(imageUrl: String) {

    val contraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_ROAMING)
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .build()


    val downlaodImageWorker = OneTimeWorkRequestBuilder<DownloadImageWorker>()
            .setInputData(workDataOf("image_path" to imageUrl))
            .setConstraints(contraints)
            .build()

    val workManager = WorkManager.getInstance(requireContext())
    workManager.enqueue(downlaodImageWorker)

    workManager.getWorkInfoByIdLiveData(downlaodImageWorker.id).observe(this, Observer { info ->

      if (info.state.isFinished == true) {
        activity?.toast("Image download!")
      }

    })



  }

  private fun getAllImages() {
    progress.visible()

    onImageUrlsReceived(listOf(Image("https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=357858179,2141091790&fm=26&gp=0.jpg")))

    return

//    networkStatusChecker.performIfConnectedToInternet {
//      GlobalScope.launch(Dispatchers.Main) {
//        val result = remoteApi.getImages()
//
//        if (result is Success) {
//          onImageUrlsReceived(result.data)
//        } else {
//          onGetImagesFailed()
//        }
//      }
//    }
  }

  private fun onImageUrlsReceived(data: List<Image>) {
    progress.gone()
    pullToRefresh.isRefreshing = false
    if (data.isNotEmpty()) noData.gone() else noData.visible()

    adapter.setData(data)
  }

  private fun onGetImagesFailed() {
    progress.gone()
    pullToRefresh.isRefreshing = false
    activity?.toast("Failed to fetch images!")
  }
}