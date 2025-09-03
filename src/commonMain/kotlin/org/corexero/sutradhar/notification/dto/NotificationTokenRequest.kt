package org.corexero.sutradhar.notification.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationTokenRequest(
    @SerialName("token") var token: String? = null,
    @SerialName("platform") var platform: String? = null,
    @SerialName("product_id") var productId: String? = null,
    @SerialName("app_id") var appId: String? = null,
    @SerialName("user_identifier") var userIdentifier: String? = null,
    @SerialName("locale") var locale: String? = null,
    @SerialName("app_version") var appVersion: String? = null
)