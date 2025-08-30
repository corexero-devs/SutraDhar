package org.corexero.sutradhar.notification.dto

import kotlinx.serialization.SerialName

data class NotificationResponse(
    @SerialName("message") var message: String? = null,
    @SerialName("id") var id: Int? = null
)
