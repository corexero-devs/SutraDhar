package org.corexero.sutradhar.utils

import android.util.Log

//do not log in release builds
actual object Logger {

    fun isDebugBuild(): Boolean {
        return isDebuggingEnabled
    }

    actual fun debug(tag: String, message: String) {
        if (!isDebugBuild()) return
        Log.d(tag, message)
    }

    actual fun info(tag: String, message: String) {
        if (!isDebugBuild()) return
        Log.i(tag, message)
    }

    actual fun warn(tag: String, message: String) {
        if (!isDebugBuild()) return
        Log.w(tag, message)
    }

    actual fun error(tag: String, message: String) {
        if (!isDebugBuild()) return
        Log.e(tag, message)
    }

    actual fun verbose(tag: String, message: String) {
        if (!isDebugBuild()) return
        Log.v(tag, message)
    }

}