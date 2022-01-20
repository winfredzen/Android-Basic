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
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.raywenderlich.android.memo.R
import com.raywenderlich.android.memo.helper.NotificationHelper
import com.raywenderlich.android.memo.helper.secondsToTime
import com.raywenderlich.android.memo.model.TimerState
import com.raywenderlich.android.memo.ui.TIMER_ACTION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

const val SERVICE_COMMAND = "Command"
const val NOTIFICATION_TEXT = "NotificationText"

// TODO: Inherit Service()
class TimerService : Service(), CoroutineScope {

    var serviceState: TimerState = TimerState.INITIALIZED

    // TODO: Use NotificationHelper with lazy delegate
    private var currentTime: Int = 0
    private var startedAtTimestamp: Int = 0
        set(value) {
            currentTime = value
            field = value
        }

    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable = object : Runnable {
        override fun run() {
            currentTime++
            broadcastUpdate()
            // Repeat every 1 second
            handler.postDelayed(this, 1000)
        }
    }
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + job

    // TODO: Define onBind(), onStartCommand() and onDestroy()

    private val helper by lazy { NotificationHelper(this) }

    private fun startTimer(elapsedTime: Int? = null) {
        serviceState = TimerState.START

        startedAtTimestamp = elapsedTime ?: 0

        // publish notification
        // TODO: Call startForeground() to publish notification
        //启动前台服务
        startForeground(NotificationHelper.NOTIFICATION_ID, helper.getNotification())


        broadcastUpdate()

        startCoroutineTimer()
    }

    private fun broadcastUpdate() {
        // update notification
        if (serviceState == TimerState.START) {
            // count elapsed time
            val elapsedTime = (currentTime - startedAtTimestamp)

            // send time to update UI
            // TODO: Send broadcast and call updateNotification
            //发送广播到Activity，更新TextView中时间
            sendBroadcast(
                Intent(TIMER_ACTION)
                    .putExtra(NOTIFICATION_TEXT, elapsedTime)
            )

            // 更新状态栏的通知
            helper.updateNotification(
                getString(R.string.time_is_running, elapsedTime.secondsToTime())
            )

        } else if (serviceState == TimerState.PAUSE) {
            // TODO: Call updateNotification if timer is paused

        }
    }

    private fun pauseTimerService() {
        serviceState = TimerState.PAUSE
        handler.removeCallbacks(runnable)
        broadcastUpdate()
    }

    private fun endTimerService() {
        serviceState = TimerState.STOP
        handler.removeCallbacks(runnable)
        job.cancel()
        broadcastUpdate()
        stopService()
    }

    private fun stopService() {
        // TODO: Call stopping service
        //停止服务
        //  tells the system that it should remove this service from foreground state
        stopForeground(true)
        // Since a service can start itself, it must handle stopping itself, as well
        stopSelf()

    }

    private fun startCoroutineTimer() {
        launch(coroutineContext) {
            handler.post(runnable)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.extras?.run {
            //判断执行什么命令
            when (getSerializable(SERVICE_COMMAND) as TimerState) {
                //启动计时器
                TimerState.START -> startTimer()
                TimerState.PAUSE -> pauseTimerService()
                TimerState.STOP -> endTimerService()
                else -> return START_NOT_STICKY
            }
        }
        //If the system kills the service because the memory runs out,
        // START_NOT_STICKY tells the system not to recreate the service with an undefined Intent
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        //移除
        handler.removeCallbacks(runnable)
        job.cancel()
    }
}