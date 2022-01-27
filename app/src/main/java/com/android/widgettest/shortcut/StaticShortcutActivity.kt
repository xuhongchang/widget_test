package com.android.widgettest.shortcut

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.android.widgettest.MainActivity
import com.android.widgettest.R
import com.android.widgettest.custom.MusicManagerService
import com.android.widgettest.util.log
import com.android.widgettest.util.showToast

class StaticShortcutActivity : AppCompatActivity() {
    companion object{
        const val TEST_INFO_KEY="测试数据"
    }
    private lateinit var createPinShortcutSuccessReceive: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_static_shortcut)
        log("StaticShortcutActivity onCreate")
        findViewById<Button>(R.id.bt_create_dynamic_shortcut).setOnClickListener {
            createDynamicShortcut()
        }
        findViewById<Button>(R.id.bt_create_pin_shortcut).setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createPinShortcut()
            }
        }
        findViewById<Button>(R.id.bt_create_pin_shortcut_exist).setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createPinShortcutWhileExist()
            }
        }
        val createPinShortcutSuccessCallBackIntent = IntentFilter("Intent.ACTION_CREATE_SHORTCUT")
        createPinShortcutSuccessReceive = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                log("createPinShortcutSuccess")
                showToast("createPinShortcutSuccess")
            }
        }
        this.registerReceiver(
            createPinShortcutSuccessReceive,
            createPinShortcutSuccessCallBackIntent
        )

        findViewById<Button>(R.id.bt_update_dynamic_shortcut).setOnClickListener {
            updateDynamicShortcut()
        }
        findViewById<Button>(R.id.bt_disable_dynamic_shortcut).setOnClickListener {
            disableDynamicShortcut()
        }
        findViewById<Button>(R.id.bt_enable_dynamic_shortcut).setOnClickListener {
            enableDynamicShortcut()
        }
        findViewById<Button>(R.id.bt_remove_dynamic_shortcut).setOnClickListener {
            removeDynamicShortcut()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.unregisterReceiver(createPinShortcutSuccessReceive)
        log("StaticShortcutActivity onDestroy")
    }

    override fun onStop() {
        super.onStop()
        log("StaticShortcutActivity onStop")
    }

    //创建动态快捷方式
    private fun createDynamicShortcut() {
        log("createDynamicShortcut")
        val shortcut = ShortcutInfoCompat.Builder(this, "dynamic_shortcut")
            .setShortLabel("Website")
            .setLongLabel("Open the website")
            .setIcon(IconCompat.createWithResource(this, R.drawable.icon))
            .setIntent(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.baidu.com")
                )
            )
            .build()

        ShortcutManagerCompat.pushDynamicShortcut(this, shortcut)
    }

    //创建固定快捷方式 id已存在（快捷方式已存在）
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

    }

    //创建固定快捷方式
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createPinShortcut() {
        log("createPinShortcut")
        val shortcutManager = getSystemService(ShortcutManager::class.java)
        //验证设备的默认启动器是否支持应用内固定快捷方式
        if (shortcutManager!!.isRequestPinShortcutSupported) {
            // Assumes there's already a shortcut with the ID "my-shortcut".
            // The shortcut must be enabled.
            val pinShortcutInfo = ShortcutInfo.Builder(this, "2pin_shortcut")
                .setShortLabel("2PinWebsite")
                .setLongLabel("1Open the website")
                .setIcon(Icon.createWithResource(this, R.drawable.icon_play))
                /*.setIntents(
                    arrayOf(
                        Intent(
                            this,
                            MainActivity::class.java
                        ).apply { action = Intent.ACTION_VIEW }, Intent(
                            Intent.ACTION_DIAL
                        ).apply { data = Uri.parse("tel:10086") })
                )*/
                .setIntent( Intent(
                    this,
                    MainActivity::class.java
                ).apply { action = Intent.ACTION_VIEW
                putExtra(TEST_INFO_KEY,"我是测试数据")})
                .build()
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
                pinnedShortcutCallbackIntent, /* flags */ 0
            )

            shortcutManager.requestPinShortcut(
                pinShortcutInfo,
                successCallback.intentSender
            )
        }

    }

    private fun updateDynamicShortcut() {
        log("updateDynamicShortcut")
        val shortcutInfoCompat = ShortcutInfoCompat.Builder(this, "dynamic_shortcut")
            .setShortLabel("Website")
            .setLongLabel("dynamic shortcut")
            .setIcon(IconCompat.createWithResource(this, R.drawable.icon_play))
            .setIntent(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.baidu.com")
                )
            )
            .build()
        ShortcutManagerCompat.updateShortcuts(this, listOf(shortcutInfoCompat))
    }

    private fun disableDynamicShortcut(){
        ShortcutManagerCompat.disableShortcuts(this, listOf("dynamic_shortcut"),"禁用动态快捷方式")
    }

    private fun enableDynamicShortcut(){
        ShortcutManagerCompat.enableShortcuts(this,listOf(ShortcutInfoCompat.Builder(this, "dynamic_shortcut")
            .setShortLabel("Website")
            .setLongLabel("Open the website")
            .setIcon(IconCompat.createWithResource(this, R.drawable.icon))
            .setIntent(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.baidu.com")
                )
            )
            .build()))
    }

    private fun removeDynamicShortcut(){
        ShortcutManagerCompat.removeDynamicShortcuts(this,listOf("dynamic_shortcut"))
    }
}