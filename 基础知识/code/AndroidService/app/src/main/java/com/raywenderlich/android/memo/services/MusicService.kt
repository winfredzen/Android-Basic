/*
 * Copyright (c) 2021 Razeware LLC
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

package com.raywenderlich.android.memo.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.raywenderlich.android.memo.R
import com.raywenderlich.android.memo.model.MusicState
import java.util.*

// TODO: Inherit Service()
class MusicService : Service() {

  private var musicState = MusicState.STOP
  private var musicMediaPlayer: MediaPlayer? = null

  private val songs: List<Int> = listOf(
      R.raw.driving_ambition,
      R.raw.beautiful_dream
  )
  private var randomSongs = mutableListOf<Int>()

  // TODO: Define MusicBinder() variable

  // TODO: Add onBind()

  fun runAction(state: MusicState) {
    musicState = state
    when (state) {
      MusicState.PLAY -> startMusic()
      MusicState.PAUSE -> pauseMusic()
      MusicState.STOP -> stopMusic()
      MusicState.SHUFFLE_SONGS -> shuffleSongs()
    }
  }

  // TODO: Add getNameOfSong()
  fun getNameOfSong(): String =
    resources.getResourceEntryName(randomSongs.first())
      .replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.ENGLISH)
        else it.toString()
      }.replace("_", " ")


  private fun initializeMediaPlayer() {
    if (randomSongs.isEmpty()) {
      randomizeSongs()
    }
    // TODO: Initialize Media Player
    musicMediaPlayer = MediaPlayer.create(this, randomSongs.first()).apply {
      isLooping = true
    }

  }

  private fun startMusic() {
    initializeMediaPlayer()
    musicMediaPlayer?.start()
  }

  private fun pauseMusic() {
    musicMediaPlayer?.pause()
  }

  private fun stopMusic() {
    musicMediaPlayer?.run {
      stop()
      release()
    }
  }

  private fun shuffleSongs() {
    musicMediaPlayer?.run {
      stop()
      release()
    }
    randomizeSongs()
    startMusic()
  }

  private fun randomizeSongs() {
    randomSongs.clear()
    randomSongs.addAll(songs.shuffled())
  }

  // TODO: create binder - MusicBinder

  override fun onBind(intent: Intent?): IBinder = binder

  private val binder by lazy { MusicBinder() }

  inner class MusicBinder : Binder() {

    fun getService(): MusicService = this@MusicService


  }


}