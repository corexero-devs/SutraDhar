package org.corexero.sutradhar.analytics

expect object FirebaseAnalyticsTracker : AnalyticsTracker {

    override fun logEvent(
        eventName: String,
        eventParams: Map<String, Any?>?
    )

    override fun setUserId(id: String)

    override fun setGlobalProperties(params: Map<String, Any>?)

}
