package org.corexero.sutradhar.remoteConfig

actual object FirebaseRemoteConfig : RemoteConfig {

    actual override fun fetchConfig() {
        getRemoteConfig()?.fetchConfig()
    }

    actual override fun getString(configKey: ConfigKey<String>): String {
        return getRemoteConfig()?.getString(configKey) ?: configKey.defaultValue
    }

    actual override fun getBoolean(configKey: ConfigKey<Boolean>): Boolean {
        return getRemoteConfig()?.getBoolean(configKey) ?: configKey.defaultValue
    }

    actual override fun getLong(configKey: ConfigKey<Long>): Long {
        return getRemoteConfig()?.getLong(configKey) ?: configKey.defaultValue
    }

    fun setUpRemoteConfig(remoteConfig: RemoteConfig) {
        this.remoteConfig = remoteConfig
    }

    private lateinit var remoteConfig: RemoteConfig

    fun getRemoteConfig(): RemoteConfig? {
        if (!::remoteConfig.isInitialized) {
            return null
        }
        return remoteConfig
    }

}