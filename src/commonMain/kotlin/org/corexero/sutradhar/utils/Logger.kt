package org.corexero.sutradhar.utils

val isDebuggingEnabled: Boolean
    get() = true

expect object Logger {

    fun debug(
        tag: String,
        message: String,
    )

    fun info(
        tag: String,
        message: String,
    )

    fun warn(
        tag: String,
        message: String,
    )

    fun error(
        tag: String,
        message: String,
    )

    fun verbose(
        tag: String,
        message: String,
    )

}