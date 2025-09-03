package org.corexero.sutradhar.appConfig

import android.content.Context

actual class AppConfigurationProviderImpl(
    private val context: Context
) {
    actual fun getAppConfiguration(): AppConfiguration {
        TODO("Not yet implemented")
    }
}