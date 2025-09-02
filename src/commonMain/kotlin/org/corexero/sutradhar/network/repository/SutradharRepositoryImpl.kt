package org.corexero.sutradhar.network.repository

import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.corexero.sutradhar.Sutradhar
import org.corexero.sutradhar.notification.NotificationTokenRequest
import org.corexero.sutradhar.notification.dto.NotificationResponse
import org.corexero.sutradhar.review.dto.FeedbackEnvelope
import org.corexero.sutradhar.review.dto.FeedbackRequest

class SutradharRepositoryImpl : SutradharRepository {

    private val http get() = Sutradhar.httpClient
    private val cfg get() = Sutradhar.config

    override suspend fun saveUserFeedback(
        feedbackRequest: FeedbackRequest
    ): Result<FeedbackEnvelope> = runCatching {
        postJson<FeedbackEnvelope>(
            baseUrl = cfg.reviewBaseUrl,
            path = "api/v1/review/add",
            bodyObj = feedbackRequest,
            headerPairs = listOf(
                "X-API-Key" to cfg.reviewApiKey()
            ) + cfg.defaultHeaders.toList()
        )
    }

    override suspend fun saveUserFCMToken(
        notificationTokenRequest: NotificationTokenRequest
    ): Result<NotificationResponse> = runCatching {
        postJson<NotificationResponse>(
            baseUrl = cfg.notificationBaseUrl,
            path = "api/v1/tokens",
            bodyObj = notificationTokenRequest,
            headerPairs = listOf(
                "x-api-key" to cfg.notificationApiKey()
            ) + cfg.defaultHeaders.toList(),
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
        val resp = http.post {
            url("${baseUrl.trimEnd('/')}/${path.trimStart('/')}")
            contentType(ContentType.Application.Json)
            headers { headerPairs.forEach { (k, v) -> append(k, v) } }
            setBody(bodyObj)
        }
        if (resp.status.value in successRange) return resp.body()
        error("HTTP ${resp.status.value}: ${resp.bodyAsText()}")
    }
}
