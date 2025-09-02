package org.corexero.sutradhar.network.repository

import org.corexero.sutradhar.notification.NotificationTokenRequest
import org.corexero.sutradhar.notification.dto.NotificationResponse
import org.corexero.sutradhar.review.dto.FeedbackEnvelope
import org.corexero.sutradhar.review.dto.FeedbackRequest

interface SutradharRepository {
    suspend fun saveUserFeedback(feedbackRequest: FeedbackRequest): Result<FeedbackEnvelope>
    suspend fun saveUserFCMToken(notificationTokenRequest: NotificationTokenRequest):
            Result<NotificationResponse>
}
