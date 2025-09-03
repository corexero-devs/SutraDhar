package org.corexero.sutradhar.deeplink

enum class DeepLinkAction(val key: String) {
    REVIEW_PLAYSTORE("review_playstore"),   // open Play Store for this/other app
    REVIEW_INAPP("review_inapp"),  // trigger in-app review
    TRIAL_REDIRECT_APP("trial"),// redirect to another app’s Play Store page
    FUNNY_MESSAGE("funny"),// show a toast
    OPEN_CUSTOM_TAB("open"), // open a URL in Chrome Custom Tabs
    OPEN_ROUTE("route"),// open route screen with source/dest station ids
    UNKNOWN("unknown");  // fallback

    companion object {
        fun from(raw: String?): DeepLinkAction {
            return values().firstOrNull { it.key == raw?.lowercase() } ?: UNKNOWN
        }
    }
}
