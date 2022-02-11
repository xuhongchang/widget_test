package com.android.widgettest

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.widgettest.shortcut.StaticShortcutActivity
import com.android.widgettest.util.log
import com.android.widgettest.util.showToast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val s =
            "${intent?.data} +${intent?.extras?.get("test_parameter")} + ${
                intent.getStringExtra("test_parameter")
            }+ ${intent.getStringExtra("room_name")} +${intent.getStringExtra("door_name")}" +
                    "+${intent.getStringExtra("lock_name")}"
        log("MainActivity onCreate+$s")
        intent?.data.apply {
            findViewById<TextView>(R.id.tv_text).text = s
        }
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
        if (intent == null) {
            return
        }
        val s =
            "${intent?.data} +${intent?.extras?.get("test_parameter")} + ${
                intent.getStringExtra("test_parameter")
            }+ ${intent.getStringExtra("room_name")} +${intent.getStringExtra("door_name")}" +
                    "+${intent.getStringExtra("lock_name")}"
        log("MainActivity onNewIntent+$s")
        intent?.data.apply {
            findViewById<TextView>(R.id.tv_text).text = s
        }
        intent?.getStringExtra(StaticShortcutActivity.TEST_INFO_KEY)?.let { showToast(it) }
    }
}