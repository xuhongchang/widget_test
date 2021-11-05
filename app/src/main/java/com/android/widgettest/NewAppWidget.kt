package com.android.widgettest

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.android.widgettest.util.SpUtil

/**
 * Implementation of App Widget functionality.
 */
class NewAppWidget : AppWidgetProvider() {
    companion object {
        const val SHOW_TOAST = "com.my.app.widget.toast"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.i(TAG, "action:$action")
        if (SHOW_TOAST == action) {
            val data = intent.data
            if (data != null) {
                val appWidgetId = data.schemeSpecificPart
                Log.i(TAG, "控件id1:$appWidgetId")
                Toast.makeText(
                    context,
                    SpUtil.getStringValue(appWidgetId.toString()),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        super.onReceive(context, intent)
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.i(TAG,"onUpdate")
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            SpUtil.deleteStringValue(appWidgetId.toString())
        }
        super.onDeleted(context, appWidgetIds)
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    RemoteViews(context.packageName, R.layout.new_app_widget).also { views ->
        views.setTextViewText(R.id.appwidget_text, SpUtil.getStringValue(appWidgetId.toString()))
        views.setOnClickPendingIntent(
            R.id.appwidget_text,
            Intent(context, NewAppWidget::class.java).run {
                data = Uri.parse("widgetid:$appWidgetId")
                Log.i(TAG, "set value $appWidgetId")
                action = NewAppWidget.SHOW_TOAST
                PendingIntent.getBroadcast(context, 0, this, 0)
            })
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}