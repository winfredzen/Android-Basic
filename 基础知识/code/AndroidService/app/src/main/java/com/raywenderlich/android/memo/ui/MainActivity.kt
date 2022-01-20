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

package com.raywenderlich.android.memo.ui

import android.content.*
import android.os.*
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.raywenderlich.android.memo.R
import com.raywenderlich.android.memo.databinding.ActivityMainBinding
import com.raywenderlich.android.memo.helper.SharedPrefs
import com.raywenderlich.android.memo.helper.onClick
import com.raywenderlich.android.memo.helper.secondsToTime
import com.raywenderlich.android.memo.model.Level
import com.raywenderlich.android.memo.model.Level.Companion.getLevel
import com.raywenderlich.android.memo.model.MusicState
import com.raywenderlich.android.memo.model.TimerState
import com.raywenderlich.android.memo.services.*
import com.raywenderlich.android.memo.view_model.MainViewModel


/**
 * Main Screen
 */

const val TIMER_ACTION = "TimerAction"

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private val sharedPrefs by lazy { SharedPrefs(this) }
    private val adapter by lazy { IconAdapter(this, mainViewModel::checkIsMatchFound) }
    private val alertBuilder by lazy { AlertDialog.Builder(this) }
    private val handler by lazy { Handler(Looper.getMainLooper()) }

    // Foreground receiver
    private val timerReceiver: TimerReceiver by lazy { TimerReceiver() }

    // Bound Service
    // TODO: Define musicService variable
    private var musicService: MusicService? = null

    // TODO: Define boundServiceConnection
    // 1
    private val boundServiceConnection = object : ServiceConnection {

        // 2
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder: MusicService.MusicBinder = service as MusicService.MusicBinder
            musicService = binder.getService()
            mainViewModel.isMusicServiceBound = true
        }

        // 3
        override fun onServiceDisconnected(arg0: ComponentName) {
            musicService?.runAction(MusicState.STOP)
            musicService = null
            mainViewModel.isMusicServiceBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Switch to AppTheme for displaying the activity
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observe()
        setUpUi()
    }

    private fun observe() {
        mainViewModel.apply {
            updateGridVisibility.observe(this@MainActivity) {
                updateVisibility()
            }
            closeCards.observe(this@MainActivity) { clickedItem ->
                handler.postDelayed({
                    adapter.revertVisibility(mainViewModel.lastOpenedCard, clickedItem)
                    mainViewModel.lastOpenedCard = null
                }, 300)
            }
            pairMatch.observe(this@MainActivity) { clickedItem ->
                handler.postDelayed({
                    adapter.pairMatch(mainViewModel.lastOpenedCard, clickedItem)
                    mainViewModel.lastOpenedCard = null
                }, 300)
            }
            showSuccessDialog.observe(this@MainActivity) {
                storeLevel()
                sendCommandToForegroundService(TimerState.STOP)
                showSuccessDialog()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // register foreground service receiver if needed
        if (!mainViewModel.isReceiverRegistered) {
            registerReceiver(timerReceiver, IntentFilter(TIMER_ACTION))
            mainViewModel.isReceiverRegistered = true
        }
    }

    override fun onPause() {
        super.onPause()
        // reset foreground service receiver if it's registered
        if (mainViewModel.isReceiverRegistered) {
            unregisterReceiver(timerReceiver)
            mainViewModel.isReceiverRegistered = false
        }
    }

    override fun onStart() {
        super.onStart()
        // bind to service if it isn't bound
        // TODO: Bind to music service
        if (!mainViewModel.isMusicServiceBound) bindToMusicService()

    }

    override fun onDestroy() {
        super.onDestroy()
        unbindMusicService()

        // if timer is running, pause timer
        if (isFinishing && mainViewModel.isForegroundServiceRunning) {
            sendCommandToForegroundService(TimerState.PAUSE)
        }
    }

    // Bound Service Methods

    // TODO: Create bindToMusicService()
    //绑定音乐服务
    private fun bindToMusicService() {
        // 1
        Intent(this, MusicService::class.java).also {
            // 2
            bindService(it, boundServiceConnection, Context.BIND_AUTO_CREATE)
        }
    }


    private fun unbindMusicService() {
        if (mainViewModel.isMusicServiceBound) {
            // stop the audio
            // TODO: Call runAction() from MusicService
                //服务解绑后，停止播放音乐
            musicService?.runAction(MusicState.STOP)


            // disconnect the service and save state
            // TODO: Call unbindService()
            unbindService(boundServiceConnection)


            mainViewModel.isMusicServiceBound = false
        }
    }

    private fun sendCommandToBoundService(state: MusicState) {
        if (mainViewModel.isMusicServiceBound) {

            // TODO: Call runAction() from MusicService
                //播放 暂停 音乐播放
            musicService?.runAction(state)


            informUser(state)
            enableButtons(state)
        } else {
            Toast.makeText(this, R.string.service_is_not_bound, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getNameOfSong() {
        val message = if (mainViewModel.isMusicServiceBound) {
            // TODO: Get song title from music service
            //getString(R.string.unknown)
                //后去音乐名称
            musicService?.getNameOfSong() ?: getString(R.string.unknown)
        } else {
            getString(R.string.service_is_not_bound)
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Foreground Service Methods

    private fun sendCommandToForegroundService(timerState: TimerState) {
        // TODO: Call starting a foreground service (timer service)
        //从Activity中启动服务
        ContextCompat.startForegroundService(this, getServiceIntent(timerState))

        mainViewModel.isForegroundServiceRunning = timerState != TimerState.STOP
    }

    private fun getServiceIntent(command: TimerState) =
        Intent(this, TimerService::class.java).apply {
            putExtra(SERVICE_COMMAND, command as Parcelable)
        }

    // UI Methods

    private fun setUpUi() {
        with(binding) {
            gridview.adapter = adapter
            //开始玩游戏的点击事件
            btnPlay.onClick {
                prepareCardView()
                sendCommandToForegroundService(TimerState.START)
            }
            btnPlayMusic.onClick {
                sendCommandToBoundService(MusicState.PLAY)
            }
            btnPauseMusic.onClick {
                sendCommandToBoundService(MusicState.PAUSE)
            }
            btnStopMusic.onClick {
                sendCommandToBoundService(MusicState.STOP)
            }
            btnShuffleMusic.onClick {
                sendCommandToBoundService(MusicState.SHUFFLE_SONGS)
            }
            btnSongName.onClick {
                getNameOfSong()
            }
            btnQuit.onClick {
                binding.tvTime.clearComposingText()
                sendCommandToForegroundService(TimerState.STOP)
            }
        }
        updateVisibility()
    }

    private fun updateVisibility() {
        with(binding) {
            val btnPlayVisible = if (mainViewModel.isForegroundServiceRunning) {
                View.INVISIBLE
            } else {
                View.VISIBLE
            }

            val gridVisible = if (mainViewModel.isForegroundServiceRunning) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
            btnPlay.apply {
                visibility = btnPlayVisible
                text = String.format(
                    getString(R.string.play_p_level),
                    getLevel(sharedPrefs.getStoredLevel())?.name
                )
            }
            gridview.visibility = gridVisible
            tvTime.visibility = gridVisible
            btnQuit.visibility = gridVisible
        }
    }

    private fun prepareCardView() {
        val currentLevel = getLevel(sharedPrefs.getStoredLevel()) ?: Level.BEGINNER

        // reset number of pairs
        mainViewModel.pairs = 0
        mainViewModel.pairsSum = currentLevel.numberOfCards

        // set columns again 更新有几列
        binding.gridview.numColumns = mainViewModel.getNumberOfColumns(currentLevel)

        // update adapter
        adapter.updateData(mainViewModel.getRandomItems(sharedPrefs.getStoredLevel()))
    }

    private fun enableButtons(state: MusicState) {
        val songPlays = state == MusicState.PLAY || state == MusicState.SHUFFLE_SONGS
        with(binding) {
            btnPlayMusic.isEnabled = !songPlays
            btnPauseMusic.isEnabled = songPlays
            btnStopMusic.isEnabled = songPlays
            btnShuffleMusic.isEnabled = songPlays
            btnSongName.apply {
                isEnabled = songPlays
                btnSongName.visibility = if (songPlays) {
                    View.VISIBLE
                } else {
                    View.INVISIBLE
                }
            }
        }
    }

    private fun updateUi(elapsedTime: Int) {
        mainViewModel.elapsedTime = elapsedTime
        binding.tvTime.text = elapsedTime.secondsToTime()
    }

    private fun checkProgress() {
        val currentLevel = getLevel(sharedPrefs.getStoredLevel())
        if (currentLevel == Level.NONE) {
            showResetProgressDialog()
        }
    }

    // increment level
    private fun storeLevel() {
        val currentLevel = getLevel(sharedPrefs.getStoredLevel())
        currentLevel?.let {
            sharedPrefs.storeLevel(
                when (it) {
                    Level.BEGINNER -> Level.INTERMEDIATE
                    Level.INTERMEDIATE -> Level.ADVANCED
                    Level.ADVANCED -> Level.EXPERT
                    Level.EXPERT -> Level.NONE
                    Level.NONE -> Level.BEGINNER
                }
            )
        }
    }

    private fun informUser(state: MusicState) {
        @StringRes val res = when (state) {
            MusicState.PLAY -> R.string.music_started
            MusicState.PAUSE -> R.string.music_paused
            MusicState.STOP -> R.string.music_stopped
            MusicState.SHUFFLE_SONGS -> R.string.songs_shuffled
        }

        Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
    }

    private fun showSuccessDialog() {
        showDialog(
            String.format(getString(R.string.well_done_your_time_is_p), mainViewModel.elapsedTime),
            getString(R.string.click_ok_for_proceeding_to_the_next_level),
        ) {
            checkProgress()
        }
    }

    private fun showResetProgressDialog() {
        showDialog(
            getString(R.string.you_have_finished_all_levels),
            getString(R.string.click_ok_to_reset_progress),
        ) {
            sharedPrefs.storeLevel(Level.BEGINNER)
        }
    }

    private fun showDialog(
        title: String,
        message: String,
        action: () -> Unit
    ) {
        with(alertBuilder)
        {
            setCancelable(false)
            setTitle(title)
            setMessage(message)
            setPositiveButton(getString(R.string.ok)) { _, _ ->
                action()
            }
            show()
        }
    }

    inner class TimerReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == TIMER_ACTION) updateUi(intent.getIntExtra(NOTIFICATION_TEXT, 0))
        }
    }
}