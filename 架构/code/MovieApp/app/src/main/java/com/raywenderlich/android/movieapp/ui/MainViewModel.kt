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

package com.raywenderlich.android.movieapp.ui

import androidx.lifecycle.*
import com.raywenderlich.android.movieapp.Event
import com.raywenderlich.android.movieapp.framework.network.MovieRepository
import com.raywenderlich.android.movieapp.framework.network.model.Movie
import com.raywenderlich.android.movieapp.ui.movies.MovieLoadingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val repository: MovieRepository) :
    ViewModel() {

    private var debouncePeriod: Long = 500
    private var searchJob: Job? = null

    //搜索的movie数据
//    val searchMoviesLiveData = MutableLiveData<List<Movie>>()

    //加载状态Loading state
    val movieLoadingStateLiveData = MutableLiveData<MovieLoadingState>()


    //    var searchMoviesLiveData: LiveData<List<Movie>>
    //用户输入的字符
    private val _searchFieldTextLiveData = MutableLiveData<String>()

    //受欢迎的movie
    private val _popularMoviesLiveData = MutableLiveData<List<Movie>>()

    //2
    val moviesMediatorData = MediatorLiveData<List<Movie>>()

    private var _searchMoviesLiveData: LiveData<List<Movie>>

    //导航到detail页面所需的数据
    private val _navigateToDetails = MutableLiveData<Event<String>>()
    val navigateToDetails: LiveData<Event<String>>
        get() = _navigateToDetails


    init {
        //当_searchFieldTextLiveData改变时，调用fetchMovieByQuery(it)
        _searchMoviesLiveData = Transformations.switchMap(_searchFieldTextLiveData) {
            fetchMovieByQuery(it)
        }

        //1
        moviesMediatorData.addSource(_popularMoviesLiveData) {
            moviesMediatorData.value = it
        }

        //2
        moviesMediatorData.addSource(_searchMoviesLiveData) {
            moviesMediatorData.value = it
        }
    }


    fun onFragmentReady() {
        //Fetch Popular Movies
        if (_popularMoviesLiveData.value.isNullOrEmpty()) {
            fetchPopularMovies()
        }
    }

    fun onSearchQuery(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(debouncePeriod)
            if (query.length > 2) {
                //获取网络数据
//                fetchMovieByQuery(query)

                //修改_searchFieldTextLiveData
                _searchFieldTextLiveData.value = query

            }
        }
    }

    //获取受欢迎的电影
    private fun fetchPopularMovies() {
        //1
        movieLoadingStateLiveData.value = MovieLoadingState.LOADING
        viewModelScope.launch(Dispatchers.IO) {
            try {
                //2
                val movies = repository.fetchPopularMovies()
                _popularMoviesLiveData.postValue(movies)

                //3
                movieLoadingStateLiveData.postValue(MovieLoadingState.LOADED)
            } catch (e: Exception) {
                //4
                movieLoadingStateLiveData.postValue(MovieLoadingState.INVALID_API_KEY)
            }
        }
    }

//    private fun fetchMovieByQuery(query: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//
//            try {
//                withContext(Dispatchers.Main) { //主线程，loading状态
//                    movieLoadingStateLiveData.value = MovieLoadingState.LOADING
//                }
//
//                val movies = repository.fetchMovieByQuery(query)
//                searchMoviesLiveData.postValue(movies)
//
//                //已加载完成了
//                movieLoadingStateLiveData.postValue(MovieLoadingState.LOADED)
//            } catch (e: Exception) {
//                //出错了
//                movieLoadingStateLiveData.postValue(MovieLoadingState.INVALID_API_KEY)
//            }
//        }
//    }

    //直接返回LiveData
    private fun fetchMovieByQuery(query: String): LiveData<List<Movie>> {
        //2
        val liveData = MutableLiveData<List<Movie>>()
        viewModelScope.launch(Dispatchers.IO) {
            val movies = repository.fetchMovieByQuery(query)
            //3
            liveData.postValue(movies)
        }
        //4
        return liveData
    }

    fun onMovieClicked(movie: Movie) {
        //点击时
        movie.title?.let {
            _navigateToDetails.value = Event(it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }
}