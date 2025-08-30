package org.corexero.sutradhar.review.dto

import kotlinx.serialization.Serializable

@Serializable
data class FeedbackEnvelope(
    val status: String,
    val message: String? = null
)
