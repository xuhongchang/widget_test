package com.android.widgettest.util

import android.util.Log
import android.widget.Toast
import com.android.widgettest.App

fun log(s: String) {
    Log.i("MyLog-${generateTag()}", s)
}

fun getCallerStackTraceElement(): StackTraceElement {
    return Thread.currentThread().stackTrace[5]
}

private fun generateTag(): String {
    val caller: StackTraceElement = getCallerStackTraceElement()
    var tag = "%s.%s(L:%d)"
    var callerClazzName = caller.className
    callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1)
    tag = String.format(tag, callerClazzName, caller.methodName, caller.lineNumber)
    return tag
}

fun showToast(s: String) {
    Toast.makeText(App.context, s, Toast.LENGTH_SHORT).show()
}