package org.corexero.sutradhar.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.corexero.sutradhar.network.NetworkConfig.NOTIFICATION_BASE_URL
import org.corexero.sutradhar.network.NetworkConfig.REVIEW_BASE_URL
import org.corexero.sutradhar.notification.NotificationTokenRequest
import org.corexero.sutradhar.notification.dto.NotificationResponse
import org.corexero.sutradhar.review.dto.FeedbackEnvelope
import org.corexero.sutradhar.review.dto.FeedbackRequest

class SutradharRepositoryImpl (
    private val httpClient: HttpClient
) : SutradharRepository {
    override suspend fun saveUserFeedback(
        feedbackRequest: FeedbackRequest
    ): Result<FeedbackEnvelope> = runCatching {
        postJson<FeedbackEnvelope>(
            baseUrl = REVIEW_BASE_URL,
            path = "api/v1/review/add",
            bodyObj = feedbackRequest,
            headerPairs = listOf("X-API-Key" to "getReviewApiKey()") // <-- no quotes around function
        )
    }

    override suspend fun saveUserFCMToken(
        notificationTokenRequest: NotificationTokenRequest
    ): Result<NotificationResponse> = runCatching {
        postJson<NotificationResponse>(
            baseUrl = NOTIFICATION_BASE_URL,
            path = "api/v1/tokens",
            bodyObj = notificationTokenRequest,
            headerPairs = listOf("x-api-key" to "getNotificationApiKey()"),
            successRange = 200..201
        )
    }

    private suspend inline fun <reified R> postJson(
        baseUrl: String,
        path: String,
        bodyObj: Any,
        headerPairs: List<Pair<String, String>> = emptyList(),
        successRange: IntRange = 200..299,
    ): R {
        val resp = httpClient.post {
            url("${baseUrl.trimEnd('/')}/${path.trimStart('/')}")
            contentType(ContentType.Application.Json)
            headers { headerPairs.forEach { (k, v) -> append(k, v) } }
            setBody(bodyObj)
        }
        if (resp.status.value in successRange) return resp.body()
        error("HTTP ${resp.status.value}: ${resp.bodyAsText()}")
    }

}
