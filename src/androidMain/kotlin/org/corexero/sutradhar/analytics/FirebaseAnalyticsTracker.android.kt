package org.corexero.sutradhar.analytics

import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import org.corexero.sutradhar.utils.Logger

actual object FirebaseAnalyticsTracker : AnalyticsTracker {

    private val firebaseAnalytics = Firebase.analytics

    actual override fun logEvent(
        eventName: String,
        eventParams: Map<String, Any?>?
    ) {
        val allParams = eventParams?.toMutableMap() ?: mutableMapOf()
        Logger.debug(
            TAG,
            "Logging event: $eventName with params: $allParams"
        )
        firebaseAnalytics.logEvent(eventName, allParams.toBundle())
    }

    actual override fun setUserId(id: String) {
        Logger.debug(TAG, "Setting user ID: $id")
        firebaseAnalytics.setUserId(id)
    }

    actual override fun setGlobalProperties(params: Map<String, Any>?) {
        Logger.debug(TAG, "Setting global properties: $params")
        firebaseAnalytics.setDefaultEventParameters(params?.toBundle())
    }

    private fun Map<String, Any?>.toBundle() = Bundle().apply {
        forEach { (key, value) ->
            if (value == null) return@forEach
            when (value::class) {
                String::class -> putString(key, value as String)
                Int::class -> putInt(key, value as Int)
                Long::class -> putLong(key, value as Long)
                Double::class -> putDouble(key, value as Double)
                Boolean::class -> putBoolean(key, value as Boolean)
            }
        }
    }

    private const val TAG = "FirebaseAnalyticsTracker"

}