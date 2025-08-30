package org.corexero.sutradhar.review.dto

import kotlinx.serialization.Serializable

@Serializable
data class FeedbackRequest(
    val productId: String,
    val feedback: String,
    val rating: Int,
    val platform: String,
    val userAgent: String,
    val userIdentifier: String,
    val topics: Set<String>,
    val appId: String
)
