package org.corexero.sutradhar.remoteConfig

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import org.corexero.sutradhar.utils.isDebuggingEnabled
import java.util.concurrent.TimeUnit

actual object FirebaseRemoteConfig : RemoteConfig {

    actual override fun fetchConfig() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (isDebuggingEnabled) {
                //fetch every five second in debug mode
                5
            } else {
                //fetch every 3 hours in production mode
                TimeUnit.HOURS.toSeconds(3)
            }
        }
        Firebase.remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
            fetchAndActivate().addOnCompleteListener {
                Log.d(TAG, "RemoteConfig Fetch complete ${it.isComplete}")
            }
        }
    }

    actual override fun getString(configKey: ConfigKey<String>): String {
        return Firebase.remoteConfig.getString(configKey.key)
    }

    actual override fun getLong(configKey: ConfigKey<Long>): Long {
        return Firebase.remoteConfig.getLong(configKey.key)
    }

    actual override fun getBoolean(configKey: ConfigKey<Boolean>): Boolean {
        return Firebase.remoteConfig.getBoolean(configKey.key)
    }

    private const val TAG = "FirebaseRemoteConfig"

}