package org.corexero.sutradhar.remoteConfig

interface RemoteConfig {

    fun fetchConfig()

    fun getString(configKey: ConfigKey<String>): String

    fun getBoolean(configKey: ConfigKey<Boolean>): Boolean

    fun getLong(configKey: ConfigKey<Long>): Long

}