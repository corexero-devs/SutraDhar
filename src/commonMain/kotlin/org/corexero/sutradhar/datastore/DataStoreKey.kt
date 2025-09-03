package org.corexero.sutradhar.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

open class DataStoreKey<T>(val key: Preferences.Key<T>, val defaultValue: T) {
    data object ShowInAppReview : DataStoreKey<Boolean>(
        key = booleanPreferencesKey("SHOW_IN_APP_REVIEW"),
        defaultValue = true
    )

    data object RecentSearches : DataStoreKey<String>(
        key = stringPreferencesKey("RECENT_SEARCHES"),
        defaultValue = ""
    )

    data object ShowUserFeedbackForm : DataStoreKey<Boolean>(
        key = booleanPreferencesKey("SHOW_USER_FEEDBACK_FORM"),
        defaultValue = true
    )

    data object LastSoftUpdateDismissedVersion : DataStoreKey<Int>(
        key = intPreferencesKey("LAST_DISMISSED_SOFT_UPDATE_VERSION"),
        defaultValue = 0
    )
}