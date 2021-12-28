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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.raywenderlich.android.movieapp.R
import com.raywenderlich.android.movieapp.framework.network.model.Movie
import kotlinx.android.synthetic.main.line_item_movie.view.*

class MovieAdapter(
    private val moviesClickListener: MoviesClickListener) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

  private var data = mutableListOf<Movie?>()

  interface MoviesClickListener {
    fun onMovieClicked(movie: Movie)
  }

  val requestOptions: RequestOptions by lazy {
    RequestOptions()
        .error(R.drawable.no_internet)
        .placeholder(R.drawable.ic_movie_placeholder)
  }

  fun updateData(newData: List<Movie?>) {
    data.clear()
    data.addAll(newData)
    notifyDataSetChanged()
  }

  inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(movie: Movie?) {
      itemView.titleTextView.text = movie?.title
      itemView.overviewTextView.text = movie?.overview

      Glide.with(itemView.context)
          .applyDefaultRequestOptions(requestOptions)
          .load("${POSTER_IMAGE_PATH_PREFIX}${movie?.posterPath}")
          .into(itemView.moviePosterImageView)

      itemView.setOnClickListener {
        movie?.let {
          moviesClickListener.onMovieClicked(it)
        }
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(
        R.layout.line_item_movie,
        parent,
        false
    )

    return MovieViewHolder(itemView)
  }

  override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
    holder.bind(data[position])
  }

  override fun getItemCount() = data.size

  companion object {
    const val POSTER_IMAGE_PATH_PREFIX = "https://image.tmdb.org/t/p/w300"
  }
}