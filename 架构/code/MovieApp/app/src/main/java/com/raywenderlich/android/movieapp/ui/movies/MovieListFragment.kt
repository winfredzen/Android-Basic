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

package com.raywenderlich.android.movieapp.ui.movies

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.raywenderlich.android.movieapp.MovieApplication.Companion.application
import com.raywenderlich.android.movieapp.R
import com.raywenderlich.android.movieapp.connectivity.ConnectivityLiveData
import com.raywenderlich.android.movieapp.framework.network.model.Movie
import com.raywenderlich.android.movieapp.ui.MainViewModel
import kotlinx.android.synthetic.main.fragment_movie_list.*
import javax.inject.Inject

class MovieListFragment : Fragment(R.layout.fragment_movie_list),
    MovieAdapter.MoviesClickListener {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var movieAdapter: MovieAdapter

    //网络监听LiveData
    private lateinit var connectivityLiveData: ConnectivityLiveData


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val searchTextWatcher = object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            // Start the search
            mainViewModel.onSearchQuery(editable.toString())
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        connectivityLiveData = ConnectivityLiveData(application)


        application.appComponent.inject(this)
        mainViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(
            MainViewModel::class.java
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initialiseObservers()
        initialiseUIElements()

        //获取受欢迎的movie
        //mainViewModel.onFragmentReady()
    }

    private fun initialiseObservers() {

//        //observer livedata
//        mainViewModel.searchMoviesLiveData.observe(viewLifecycleOwner, Observer {
//            movieAdapter.updateData(it)
//        })

        //数据
        mainViewModel.moviesMediatorData.observe(viewLifecycleOwner, Observer {
            movieAdapter.updateData(it)
        })

        //加载状态 livedata
        mainViewModel.movieLoadingStateLiveData.observe(viewLifecycleOwner, Observer {
            onMovieLoadingStateChanged(it)
        })

        //网络
        connectivityLiveData.observe(viewLifecycleOwner, Observer { isAvailable ->
            //2
            when (isAvailable) {
                true -> {
                    //3
                    mainViewModel.onFragmentReady()
                    statusButton.visibility = View.GONE
                    moviesRecyclerView.visibility = View.VISIBLE
                    searchEditText.visibility = View.VISIBLE
                }
                false -> {
                    statusButton.visibility = View.VISIBLE
                    moviesRecyclerView.visibility = View.GONE
                    searchEditText.visibility = View.GONE
                }
            }
        })

        //导航至详情页面
        mainViewModel.navigateToDetails.observe(viewLifecycleOwner, Observer {
//            findNavController().navigate(MovieListFragmentDirections.actionMovieClicked(it))

            it.getContentIfNotHandled()?.let {
                findNavController().navigate(MovieListFragmentDirections.actionMovieClicked(it))
            }

        })

    }

    private fun initialiseUIElements() {
        searchEditText.addTextChangedListener(searchTextWatcher)
        movieAdapter = MovieAdapter(this)
        moviesRecyclerView.apply {
            adapter = movieAdapter
            hasFixedSize()
        }
    }

    override fun onMovieClicked(movie: Movie) {
        mainViewModel.onMovieClicked(movie)
    }

    private fun onMovieLoadingStateChanged(state: MovieLoadingState) {
        when (state) {
            MovieLoadingState.LOADING -> {
                statusButton.visibility = View.GONE
                moviesRecyclerView.visibility = View.GONE
                loadingProgressBar.visibility = View.VISIBLE
            }
            MovieLoadingState.LOADED -> {
                //修改旋转时显示的bug
                connectivityLiveData.value?.let {
                    if (it) {
                        statusButton.visibility = View.GONE
                        moviesRecyclerView.visibility = View.VISIBLE
                    } else {
                        statusButton.visibility = View.VISIBLE
                        moviesRecyclerView.visibility = View.GONE
                    }
                }
                loadingProgressBar.visibility = View.GONE
            }
            MovieLoadingState.ERROR -> {
                statusButton.visibility = View.VISIBLE
                context?.let {
                    statusButton.setCompoundDrawables(
                        null, ContextCompat.getDrawable(it, R.drawable.no_internet), null,
                        null
                    )
                }
                moviesRecyclerView.visibility = View.GONE
                loadingProgressBar.visibility = View.GONE
            }
            MovieLoadingState.INVALID_API_KEY -> {
                statusButton.visibility = View.VISIBLE
                statusButton.text = getString(R.string.invalid_api_key)
                statusButton.setCompoundDrawables(null, null, null, null)
                moviesRecyclerView.visibility = View.GONE
                loadingProgressBar.visibility = View.GONE
            }
        }
    }
}