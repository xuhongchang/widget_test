package com.android.widgettest.custom

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.widget.RemoteViews
import com.android.widgettest.R
import com.android.widgettest.TAG

class MusicAppWidget : AppWidgetProvider() {

    companion object {
        const val ACTION_UPDATE_UI = "action_update_ui"
        const val KEY_UI_PLAY_BTN = "music.appwidget.play.status"
        const val KEY_UI_PLAY_NAME = "music.appwidget.play.name"
        private var mStop = true

    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.i(TAG, "action:$action")
        if (intent.hasCategory(Intent.CATEGORY_ALTERNATIVE)) {
            val data = intent.getData()
            var resID = -1
            if (data != null) {
                resID = Integer.parseInt(data.getSchemeSpecificPart())
            }
            when (resID) {
                R.id.iv_play -> {
                    Log.i(TAG, "play ：$mStop")
                    if (mStop) {
                        mStop = false
                        val startIntent = Intent(context, MusicManagerService::class.java)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            context.startForegroundService(startIntent)
                        } else {
                            context.startService(startIntent)
                        }
                    } else {
                        Log.i(TAG, "更新播放状态")
                        controlPlay(context, MusicManagerService.ACTION_PLAY_PAUSE)
                    }
                }
                R.id.iv_enable -> {
                    //关闭服务 也会关闭前台通知
                    /*val startIntent = Intent(context, MusicManagerService::class.java)
                    context.stopService(startIntent)*/
                    controlPlay(context, MusicManagerService.ACTION_PLAY_STOP)
                }
                else -> {
                }
            }

        } else {
            val playPause = intent.getBooleanExtra(KEY_UI_PLAY_BTN, false)
            val songId = intent.getStringExtra(KEY_UI_PLAY_NAME)
            val remoteViews = RemoteViews(context.packageName, R.layout.music_app_widget).apply {
                setTextViewText(R.id.tv_name, if (TextUtils.isEmpty(songId)) "明天你好" else songId)
                setImageViewResource(
                    R.id.iv_play,
                    if (playPause) R.drawable.preview_btn_stop_focus else R.drawable.preview_btn_play_focus
                )
                setOnClickPendingIntent(
                    R.id.iv_play,
                    Intent(context, MusicAppWidget::class.java).run {
                        addCategory(Intent.CATEGORY_ALTERNATIVE)
                        data = Uri.parse("hx:" + R.id.iv_play)
                        PendingIntent.getBroadcast(context, 0, this, 0)
                    })
                setOnClickPendingIntent(
                    R.id.iv_enable,
                    Intent(context, MusicAppWidget::class.java).run {
                        addCategory(Intent.CATEGORY_ALTERNATIVE)
                        data = Uri.parse("hx:" + R.id.iv_enable)
                        PendingIntent.getBroadcast(context, 0, this, 0)
                    })
            }
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context, MusicAppWidget::class.java)
            appWidgetManager.updateAppWidget(componentName, remoteViews)

        }
        super.onReceive(context, intent)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.i(TAG, "music widget onUpdate")

        for (appWidgetId in appWidgetIds) {
            val remoteViews = RemoteViews(context.packageName, R.layout.music_app_widget).apply {
                setOnClickPendingIntent(
                    R.id.iv_play,
                    Intent(context, MusicAppWidget::class.java).run {
                        addCategory(Intent.CATEGORY_ALTERNATIVE)
                        data = Uri.parse("hx:" + R.id.iv_play)
                        PendingIntent.getBroadcast(context, 0, this, 0)
                    })
                setOnClickPendingIntent(
                    R.id.iv_enable,
                    Intent(context, MusicAppWidget::class.java).run {
                        addCategory(Intent.CATEGORY_ALTERNATIVE)
                        data = Uri.parse("hx:" + R.id.iv_enable)
                        PendingIntent.getBroadcast(context, 0, this, 0)
                    })
            }
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews)

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    //发送广播到MusicManagerService控制播放
    private fun controlPlay(context: Context, ACTION: Int) {
        Log.i(TAG, "发送广播到MusicManagerService控制播放")
        val actionIntent = Intent(MusicManagerService.ACTION_CONTROL_PLAY)
        actionIntent.putExtra(MusicManagerService.KEY_USR_ACTION, ACTION)
        context.sendBroadcast(actionIntent);
    }

}