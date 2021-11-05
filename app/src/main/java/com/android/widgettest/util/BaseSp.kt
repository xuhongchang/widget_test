package com.android.widgettest.util

import android.content.Context
import android.util.Log
import com.android.widgettest.App
import com.android.widgettest.TAG

abstract class BaseSp {
    abstract fun getSpName(): String
    private fun sharedPreferences() =
        App.context.getSharedPreferences(getSpName(), Context.MODE_PRIVATE)

    protected fun setValue(bean: Pair<String, *>) {
        sharedPreferences().edit().apply() {
            when (bean.second) {
                is Boolean -> this.putBoolean(bean.first, bean.second as Boolean)
                is Int -> this.putInt(bean.first, bean.second as Int)
                is Long -> this.putLong(bean.first, bean.second as Long)
                is Float -> this.putFloat(bean.first, bean.second as Float)
                is String -> this.putString(bean.first, bean.second as String)
                else -> Log.d(TAG, "保存错误，请使用正确的值")
            }
            apply()
        }
    }

    protected fun <T> getValue(key: String, defaultValue: T): T {
        return sharedPreferences().run {
            when (defaultValue) {
                is Boolean -> getBoolean(key, defaultValue)
                is Int -> getInt(key, defaultValue)
                is Long -> getLong(key, defaultValue)
                is Float -> getFloat(key, defaultValue)
                is String -> getString(key, defaultValue)
                else -> defaultValue
            } as T
        }
    }

    protected fun deleteValue(key: String) {
        sharedPreferences().edit().apply {
            remove(key)
            apply()
        }
    }

    protected fun isContain(key: String) = sharedPreferences().contains(key)

}