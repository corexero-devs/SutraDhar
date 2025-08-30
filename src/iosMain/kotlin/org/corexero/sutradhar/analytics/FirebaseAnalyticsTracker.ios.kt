package org.corexero.sutradhar.analytics

import org.corexero.sutradhar.utils.Logger
import kotlin.collections.filter

actual object FirebaseAnalyticsTracker : AnalyticsTracker {

    actual override fun logEvent(
        eventName: String,
        eventParams: Map<String, Any?>?
    ) {
        Logger.debug(
            TAG,
            "Logging event: $eventName with params: $eventParams"
        )
        getAnalyticsTracker()?.logEvent(
            eventName,
            eventParams?.filter { it.value == null } ?: emptyMap()
        )
    }

    actual override fun setUserId(id: String) {
        Logger.debug(TAG, "Setting user ID: $id")
        getAnalyticsTracker()?.setUserId(id)
    }

    actual override fun setGlobalProperties(params: Map<String, Any>?) {
        Logger.debug(TAG, "Setting global properties: $params")
        getAnalyticsTracker()?.setGlobalProperties(params ?: emptyMap())
    }

    private const val TAG = "FirebaseAnalyticsTracker"

    lateinit var analyticsTracker: AnalyticsTracker

    private fun getAnalyticsTracker(): AnalyticsTracker? {
        if (::analyticsTracker.isInitialized) {
            return analyticsTracker
        }
        return null
    }

}