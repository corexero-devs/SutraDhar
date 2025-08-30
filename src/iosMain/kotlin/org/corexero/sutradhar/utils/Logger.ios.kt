package org.corexero.sutradhar.utils

import platform.Foundation.NSLog

actual object Logger {

    fun isDebugBuild(): Boolean {
        return isDebuggingEnabled
    }

    actual fun debug(tag: String, message: String) {
        if (!isDebugBuild()) return
        println("DEBUG: [$tag] $message")
        NSLog("DEBUG: [$tag] $message")
    }

    actual fun info(tag: String, message: String) {
        if (!isDebugBuild()) return
        println("INFO: [$tag] $message")
        NSLog("INFO: [$tag] $message")
    }

    actual fun warn(tag: String, message: String) {
        if (!isDebugBuild()) return
        println("WARN: [$tag] $message")
        NSLog("WARN: [$tag] $message")
    }

    actual fun error(tag: String, message: String) {
        if (!isDebugBuild()) return
        println("ERROR: [$tag] $message")
        NSLog("ERROR: [$tag] $message")
    }

    actual fun verbose(tag: String, message: String) {
        if (!isDebugBuild()) return
        println("VERBOSE: [$tag] $message")
        NSLog("VERBOSE: [$tag] $message")
    }
}