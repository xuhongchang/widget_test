package com.android.widgettest.shortcut

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.widgettest.util.log

/**
 * 这意味着，如果应用已在运行，则在静态快捷方式启动时，应用中的所有现有 Activity 都会被销毁。如果不希望出现这种行为，
 * 您可以使用 Trampoline Activity，或者使用一个先在 Activity.onCreate(Bundle)
 * 中启动其他 Activity、而后调用 Activity.finish() 的不可见 Activity：
 */
class TrampolineActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log("onCreate")
        startActivity(Intent(this, StaticShortcutActivity::class.java))
        finish()
    }
}