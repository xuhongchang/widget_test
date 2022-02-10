package com.android.widgettest

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.android.widgettest.util.showToast

/**
 * 创建快捷方式 用户确认是否允许的自定义弹窗
 * 当前测试下来没有用
 */
class DialogActivity :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_dialog)
        findViewById<Button>(R.id.add_button).setOnClickListener {
            showToast("create shortcut")
        }
    }
}