package com.android.widgettest

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.android.widgettest.util.showToast

class DialogActivity :AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_dialog)
        findViewById<Button>(R.id.add_button).setOnClickListener {
            showToast("create shortcut")
        }
    }
}