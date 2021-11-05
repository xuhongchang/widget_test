package com.android.widgettest

import android.app.Activity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import androidx.appcompat.app.AppCompatActivity
import com.android.widgettest.databinding.ActivityNewAppWidgetConfigBinding
import com.android.widgettest.util.SpUtil
import kotlin.properties.Delegates

class NewAppWidgetConfigActivity : AppCompatActivity() {

    private var appWidgetId by Delegates.notNull<Int>()
    private lateinit var viewBind: ActivityNewAppWidgetConfigBinding
    private lateinit var name: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBind = ActivityNewAppWidgetConfigBinding.inflate(layoutInflater)
        setContentView(viewBind.root)
        appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        Log.i(TAG, "控件id:$appWidgetId")
        doConfig()
    }

    private fun doConfig() {

        viewBind.btSave.setOnClickListener {
            name = viewBind.etName.text.toString()
            SpUtil.setStringValue(appWidgetId.toString(), name)
            update()
        }
    }

    private fun update() {
        //配置完成后，通过调用 getInstance(Context) 来获取 AppWidgetManager 的实例
        val appWidgetManager: AppWidgetManager = AppWidgetManager.getInstance(this)

        //通过调用 updateAppWidget(int, RemoteViews) 来使用 RemoteViews 布局更新应用微件
        RemoteViews(packageName, R.layout.new_app_widget).also { views ->
            views.setTextViewText(R.id.appwidget_text, name)
            views.setOnClickPendingIntent(
                R.id.appwidget_text,
                Intent(this, NewAppWidget::class.java).run {
                    data = Uri.parse("widgetid:$appWidgetId")
                    Log.i(TAG, "set value $appWidgetId")
                    action = NewAppWidget.SHOW_TOAST
                    PendingIntent.getBroadcast(this@NewAppWidgetConfigActivity, 0, this, 0)
                })
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        //创建返回 Intent，为其设置 Activity 结果，然后结束该 Activity
        val resultValue = Intent().apply {
            putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        }
        setResult(Activity.RESULT_OK, resultValue)
        finish()
        //当配置 Activity 首次打开时，请将 Activity 结果设为 RESULT_CANCELED 并注明 EXTRA_APPWIDGET_ID，
        // 如上面的第 5 步所示。这样，如果用户在到达末尾之前退出该 Activity，应用微件托管应用就会收到配置已取消的通知，因此不会添加应用微件。


    }

    override fun onBackPressed() {
        super.onBackPressed()
        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if they press the back button.
        setResult(RESULT_CANCELED);
    }
}