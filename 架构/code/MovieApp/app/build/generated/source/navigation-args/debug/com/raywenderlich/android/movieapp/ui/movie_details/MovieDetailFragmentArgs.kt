package com.raywenderlich.android.movieapp.ui.movie_details

import android.os.Bundle
import androidx.navigation.NavArgs
import java.lang.IllegalArgumentException
import kotlin.String
import kotlin.jvm.JvmStatic

data class MovieDetailFragmentArgs(
  val movieName: String
) : NavArgs {
  fun toBundle(): Bundle {
    val result = Bundle()
    result.putString("movie_name", this.movieName)
    return result
  }

  companion object {
    @JvmStatic
    fun fromBundle(bundle: Bundle): MovieDetailFragmentArgs {
      bundle.setClassLoader(MovieDetailFragmentArgs::class.java.classLoader)
      val __movieName : String?
      if (bundle.containsKey("movie_name")) {
        __movieName = bundle.getString("movie_name")
        if (__movieName == null) {
          throw IllegalArgumentException("Argument \"movie_name\" is marked as non-null but was passed a null value.")
        }
      } else {
        throw IllegalArgumentException("Required argument \"movie_name\" is missing and does not have an android:defaultValue")
      }
      return MovieDetailFragmentArgs(__movieName)
    }
  }
}
