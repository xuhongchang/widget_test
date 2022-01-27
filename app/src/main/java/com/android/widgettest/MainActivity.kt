package com.android.widgettest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.widgettest.shortcut.StaticShortcutActivity
import com.android.widgettest.util.log
import com.android.widgettest.util.showToast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        log("MainActivity onCreate+${intent?.data}")
    }

    override fun onStop() {
        super.onStop()
        log("MainActivity onStop")

    }

    override fun onDestroy() {
        super.onDestroy()
        log("MainActivity onDestroy")

    }

    override fun onBackPressed() {
        moveTaskToBack(false)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        log("MainActivity onNewIntent+${intent?.data}")
        intent?.getStringExtra(StaticShortcutActivity.TEST_INFO_KEY)?.let { showToast(it) }
    }
}