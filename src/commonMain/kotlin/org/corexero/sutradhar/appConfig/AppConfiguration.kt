package org.corexero.sutradhar.appConfig

import org.corexero.sutradhar.network.NetworkConfig

data class AppConfiguration(
    val productId: String,
    val reviewBaseUrl: String = NetworkConfig.REVIEW_BASE_URL,
    val notificationBaseUrl: String = NetworkConfig.NOTIFICATION_BASE_URL,
    val dbPlayIntegrityBaseUrl: String = NetworkConfig.PLAY_INTEGRITY_BASE_URL,
    val reviewApiKey: String,
    val notificationApiKey: String,
    val notificationIcon: Int,
    val platformUserAgent: String,
    val packageName: String,
    val defaultHeaders: Map<String, String> = emptyMap()
)
