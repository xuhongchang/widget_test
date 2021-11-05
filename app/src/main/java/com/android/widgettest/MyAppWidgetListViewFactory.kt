package com.android.widgettest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
const val TAG="MyAppWidget"
class MyAppWidgetListViewFactory(private val context: Context,p0: Intent):RemoteViewsService.RemoteViewsFactory {
    private val list:ArrayList<String> by lazy { ArrayList() }
    override fun onCreate() {
        Log.i(TAG,"MyAppWidgetListViewFactory onCreate")
        list.apply {
            add("星期一")
            add("星期二")
            add("星期三")
            add("星期四")
            add("星期五")
            add("星期六")
            add("星期日")
        }
        Thread.sleep(3000)
    }

    override fun onDataSetChanged() {
    }

    override fun onDestroy() {
        list.clear()
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getViewAt(p0: Int): RemoteViews {
        val bundle = Bundle().apply {
            putInt(MyAppwidget.EXTRA_ITEM, p0)
        }
        val fillIntent = Intent().apply { putExtras(bundle) }
        Thread.sleep(500)

        return RemoteViews(context.packageName, R.layout.item_my_widget).apply {
            setTextViewText(R.id.tv_text, list[p0])
            setOnClickFillInIntent(R.id.ll_bg, fillIntent)
        }

    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }
}