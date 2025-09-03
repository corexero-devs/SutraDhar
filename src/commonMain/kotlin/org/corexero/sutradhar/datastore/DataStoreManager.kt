package org.corexero.sutradhar.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreManager(
    private val dataStore: DataStore<Preferences>
) {

    fun <T> getFlow(dataStoreKey: DataStoreKey<T>): Flow<T> =
        dataStore.data.map { preferences ->
            val result = preferences[dataStoreKey.key] ?: dataStoreKey.defaultValue
            result
        }

    suspend fun <T> getFirst(dataStoreKey: DataStoreKey<T>): T {
        return dataStore.data.first()[dataStoreKey.key] ?: dataStoreKey.defaultValue
    }

    suspend fun <T> put(dataStoreKey: DataStoreKey<T>, value: T) {
        dataStore.edit {
            it[dataStoreKey.key] = value
        }
    }

}


