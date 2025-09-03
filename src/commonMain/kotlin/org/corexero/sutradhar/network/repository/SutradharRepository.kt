package org.corexero.sutradhar.network.repository

import org.corexero.sutradhar.notification.dto.NotificationResponse
import org.corexero.sutradhar.notification.dto.NotificationTokenRequest
import org.corexero.sutradhar.playIntegrity.IntegrityTokenProvider
import org.corexero.sutradhar.playIntegrity.dto.DbMetaResponse
import org.corexero.sutradhar.playIntegrity.dto.DbRequest
import org.corexero.sutradhar.review.dto.FeedbackEnvelope
import org.corexero.sutradhar.review.dto.FeedbackRequest

interface SutradharRepository {
    suspend fun saveUserFeedback(feedbackRequest: FeedbackRequest): Result<FeedbackEnvelope>
    suspend fun saveUserFCMToken(notificationTokenRequest: NotificationTokenRequest):
            Result<NotificationResponse>

    suspend fun fetchDbIfChanges(
        tokenProvider: IntegrityTokenProvider,
        dbRequest: DbRequest
    ): Result<DbMetaResponse>
}
