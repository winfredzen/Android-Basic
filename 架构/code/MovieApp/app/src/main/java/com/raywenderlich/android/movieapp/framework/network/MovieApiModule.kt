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

package com.raywenderlich.android.movieapp.framework.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://api.themoviedb.org/3/"
private const val TMDb_API_KEY = "f93058cb73afcc6b0fddb64076856113"

@Module
class MovieApiModule {

  @Provides
  @Singleton
  fun provideInterceptor(): Interceptor {
    return Interceptor { chain ->
      val newUrl = chain.request().url()
          .newBuilder()
          .addQueryParameter("api_key", TMDb_API_KEY)
          .build()

      val newRequest = chain.request()
          .newBuilder()
          .url(newUrl)
          .build()

      chain.proceed(newRequest)
    }
  }

  @Provides
  @Singleton
  fun provideWeatherApiClient(authInterceptor: Interceptor): OkHttpClient {
    return OkHttpClient().newBuilder()
        .addInterceptor(authInterceptor)
        .build()
  }

  @Provides
  @Singleton
  fun provideMoshi(): Moshi {
    return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
  }

  @Provides
  @Singleton
  fun provideRetrofit(moshi: Moshi, randomUserApiClient: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .client(randomUserApiClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()
  }

  @Provides
  @Singleton
  fun providesWeatherApi(retrofit: Retrofit): MovieService {
    return retrofit.create(MovieService::class.java)
  }
}