package org.corexero.sutradhar.analytics

interface AnalyticsTracker {

    fun logEvent(eventName: String, eventParams: Map<String, Any?>? = null)

    fun setGlobalProperties(params: Map<String, Any>?)

    fun setUserId(id: String)

}