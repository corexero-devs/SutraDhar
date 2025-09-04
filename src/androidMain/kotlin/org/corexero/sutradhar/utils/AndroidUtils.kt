package org.corexero.sutradhar.utils

import android.content.Context
import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri

object AndroidUtils {

    fun openPlayStore(context: Context, pkg: String) {
        val market = Intent(
            Intent.ACTION_VIEW,
            "market://details?id=$pkg".toUri()
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        val web = Intent(
            Intent.ACTION_VIEW,
            "https://play.google.com/store/apps/details?id=$pkg".toUri()
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            context.startActivity(market)
        } catch (_: Exception) {
            context.startActivity(web)
        }
    }

    fun openCustomTab(context: Context, url: String) {
        val uri = url.toUri()
        val customTabsIntent = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()

        customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        customTabsIntent.launchUrl(context, uri)
    }
}
