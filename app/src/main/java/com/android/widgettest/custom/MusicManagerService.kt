package com.android.widgettest.custom

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.android.widgettest.R
import com.android.widgettest.TAG

class MusicManagerService : Service() {
    companion object {
        const val ACTION_CONTROL_PLAY = "action_control_play"
        const val KEY_USR_ACTION = "key_usr_action"
        const val ACTION_PRE = 0
        const val ACTION_PLAY_PAUSE = 1
        const val ACTION_NEXT = 2
        const val ACTION_PLAY_STOP = 3
    }

    private var playStatus = false
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context, intent: Intent) {
            Log.i(TAG, "service onReceive")

            val widgetAction: Int = intent.getIntExtra(KEY_USR_ACTION, -1)
            when (widgetAction) {
                ACTION_PLAY_PAUSE -> if (playStatus) pause(p0) else play(p0)
                ACTION_PLAY_STOP -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) stopForeground(
                    true
                )
                else -> {
                }
            }
        }

    }

    private val mediaPlayer by lazy { MediaPlayer.create(applicationContext, R.raw.ring) }
    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "service onCreate")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val NOTIFICATION_CHANNEL_ID = "package_name";
            val channelName = "My Background Service";
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
            val notification = Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.icon)  // the status icon
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentText("Music服务正在运行")  // the contents of the entry
                .build();

            startForeground(2, notification);
        }


        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_CONTROL_PLAY)
        registerReceiver(receiver, intentFilter)
        mediaPlayerInit()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "service onDestroy")
        playStatus = false
        mediaPlayer.stop()
        unregisterReceiver(receiver)
    }

    private fun mediaPlayerInit() {
        playStatus = true
        mediaPlayer.isLooping = true
        mediaPlayer.start()
        updateUI(applicationContext)

    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }


    private fun play(context: Context) {
        playStatus = true
        mediaPlayer.start()
        updateUI(context)
    }

    private fun pause(context: Context) {
        playStatus = false
        mediaPlayer.pause()
        updateUI(context)
    }

    private fun updateUI(context: Context) {
        Log.i(TAG, "service updateUI $playStatus $packageName")
        //注意这个intent构造的是显式intent，所以我们的广播不需要注册action就会发到这们这个类MusicAppWidget
        val actionIntent = Intent(context, MusicAppWidget::class.java).apply {
            action = MusicAppWidget.ACTION_UPDATE_UI
            putExtra(MusicAppWidget.KEY_UI_PLAY_BTN, playStatus)
            putExtra(MusicAppWidget.KEY_UI_PLAY_NAME, "Ring")
            //8.0以后必须要加这段代码不然收不到广播
            //包名、接受者receiver 所在路径
            /* component = ComponentName(
                 packageName,
                 "com.android.widgettest.custom.MusicAppWidget"
             )*/
        }
        context.sendBroadcast(actionIntent)
    }
}