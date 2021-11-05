package com.android.widgettest.util

import android.util.Log
import com.android.widgettest.TAG

object SpUtil : BaseSp() {
    override fun getSpName(): String {
        return "config"
    }

    fun setStringValue(key: String, value: String) {
        Log.i(TAG, "key:$key  value:$value")
        setValue(Pair(key, value))
    }

    fun getStringValue(key: String) = getValue(key, "")

    fun deleteStringValue(key: String) = deleteValue(key)
}