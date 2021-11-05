package com.android.widgettest

import android.content.Intent
import android.widget.RemoteViewsService

class MyAppWidgetListViewService:RemoteViewsService() {
    override fun onGetViewFactory(p0: Intent): RemoteViewsFactory {
        return MyAppWidgetListViewFactory(applicationContext,p0)
    }
}