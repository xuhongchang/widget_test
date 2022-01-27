package com.android.widgettest.shortcut

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.android.widgettest.R
import com.android.widgettest.util.log

class CustomShortcutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_shortcut)
        log("onCreate")
        AlertDialog.Builder(this).setTitle("添加快捷方式")
            .setMessage("确认添加快捷方式吗")
            .setPositiveButton("confirm") { _, _ ->
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
                    createPinShortcutWhileExist()
                }
            }
            .show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createPinShortcutWhileExist() {
        log("createPinShortcutWhileExist")
        val shortcutManager = getSystemService(ShortcutManager::class.java)
        //验证设备的默认启动器是否支持应用内固定快捷方式
        if (shortcutManager!!.isRequestPinShortcutSupported) {
            // Assumes there's already a shortcut with the ID "my-shortcut".
            // The shortcut must be enabled.
            val pinShortcutInfo = ShortcutInfo.Builder(this, "dynamic_shortcut").build()

            // Create the PendingIntent object only if your app needs to be notified
            // that the user allowed the shortcut to be pinned. Note that, if the
            // pinning operation fails, your app isn't notified. We assume here that the
            // app has implemented a method called createShortcutResultIntent() that
            // returns a broadcast intent.
            val pinnedShortcutCallbackIntent =
                shortcutManager.createShortcutResultIntent(pinShortcutInfo)

            // Configure the intent so that your app's broadcast receiver gets
            // the callback successfully.For details, see PendingIntent.getBroadcast().
            val successCallback = PendingIntent.getBroadcast(
                this, /* request code */ 0,
                Intent("Intent.ACTION_CREATE_SHORTCUT"), /* flags */ 0
            )

            shortcutManager.requestPinShortcut(
                pinShortcutInfo,
                successCallback.intentSender
            )
        }

        setResult(RESULT_OK, Intent())
    }
}