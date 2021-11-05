package com.android.widgettest

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import android.widget.Toast
import java.util.concurrent.Executors


class MyAppwidget : AppWidgetProvider() {
    companion object{
        const val TOAST_ACTION = "com.example.android.stackwidget.TOAST_ACTION"
        const val EXTRA_ITEM = "com.example.android.stackwidget.EXTRA_ITEM"
    }
    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == TOAST_ACTION) {
            val appWidgetId: Int = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
            val viewIndex: Int = intent.getIntExtra(EXTRA_ITEM, 0)
            Toast.makeText(context, "Touched view $viewIndex", Toast.LENGTH_SHORT).show()
        }
       /* // 获得appwidget管理实例，用于管理appwidget以便进行更新操作
        val mgr: AppWidgetManager = AppWidgetManager.getInstance(context)
        // 相当于获得所有本程序创建的appwidget
        val componentName = ComponentName(context, MyAppwidget::class.java)

        val remoteView = RemoteViews(context.packageName, R.layout.widget_my)
        // 更新appwidget
        mgr.updateAppWidget(componentName, remoteView)*/

        super.onReceive(context, intent)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        for (appwidget in appWidgetIds) {
            val pendingIntent: PendingIntent =
                Intent(context, MainActivity::class.java).let { intent ->
                    PendingIntent.getActivity(context, 0, intent, 0)
                }
            val text = context.getString(R.string.app_name)

            val intentService=Intent(context,MyAppWidgetListViewService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appwidget)
                // When intents are compared, the extras are ignored, so we need to embed the extras
                // into the data so that the extras will not be ignored.、
                // 通过putExtra 传值会被intent对比的时候忽略 导致每次getExtra..一直是同一个
                //所以使用data传值
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }

            //注意这个intent构造的是显式intent，所以我们的广播不需要注册action就会发到这们这个类MyAppwidget，
            val toastPendingIntent: PendingIntent = Intent(
                context,
                MyAppwidget::class.java
            ).run {
                // Set the action for the intent.
                // When the user touches a particular view, it will have the effect of
                // broadcasting TOAST_ACTION.
                action = TOAST_ACTION
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appwidget)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))

                PendingIntent.getBroadcast(context, 0, this, PendingIntent.FLAG_UPDATE_CURRENT)
            }

            val remoteView = RemoteViews(context.packageName, R.layout.widget_my).apply {
                //设置文本
                setTextViewText(R.id.tv_my, text)
                //点击事件
                setOnClickPendingIntent(R.id.tv_my, pendingIntent)

                //listView
                setRemoteAdapter(R.id.lv_main,intentService)
                setEmptyView(R.id.lv_main,R.id.iv_empty)
                //listView 的点击模板
                setPendingIntentTemplate(R.id.lv_main, toastPendingIntent)
            }
            appWidgetManager.updateAppWidget(appwidget, remoteView)

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }
}