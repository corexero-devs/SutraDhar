package org.corexero.sutradhar.remoteConfig

expect object FirebaseRemoteConfig : RemoteConfig {

    override fun fetchConfig()

    override fun getString(configKey: ConfigKey<String>): String

    override fun getBoolean(configKey: ConfigKey<Boolean>): Boolean

    override fun getLong(configKey: ConfigKey<Long>): Long

}