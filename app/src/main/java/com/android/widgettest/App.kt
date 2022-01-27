package com.android.widgettest

import android.app.Application
import com.android.widgettest.util.log

class App : Application() {
    companion object {
        lateinit var context: Application
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        log("app create")
    }
}