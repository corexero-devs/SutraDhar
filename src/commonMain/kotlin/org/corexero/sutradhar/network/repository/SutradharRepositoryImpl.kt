package org.corexero.sutradhar.network.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.headers
import org.corexero.sutradhar.appConfig.AppConfigurationProvider
import org.corexero.sutradhar.notification.dto.NotificationResponse
import org.corexero.sutradhar.notification.dto.NotificationTokenRequest
import org.corexero.sutradhar.playIntegrity.IntegrityTokenProvider
import org.corexero.sutradhar.playIntegrity.dto.DbMetaResponse
import org.corexero.sutradhar.playIntegrity.dto.DbRequest
import org.corexero.sutradhar.review.dto.FeedbackEnvelope
import org.corexero.sutradhar.review.dto.FeedbackRequest

class SutradharRepositoryImpl(
    private val httpClient: HttpClient,
    private val appConfigurationProvider: AppConfigurationProvider
) : SutradharRepository {

    private val appConfiguration by lazy {
        appConfigurationProvider.getAppConfiguration()
    }

    override suspend fun saveUserFeedback(
        feedbackRequest: FeedbackRequest
    ): Result<FeedbackEnvelope> = runCatching {
        postJson<FeedbackEnvelope>(
            baseUrl = appConfiguration.reviewBaseUrl,
            path = "api/v1/review/add",
            bodyObj = feedbackRequest,
            headerPairs = listOf(
                "X-API-Key" to appConfiguration.reviewApiKey
            ) + appConfiguration.defaultHeaders.toList()
        )
    }

    override suspend fun saveUserFCMToken(
        notificationTokenRequest: NotificationTokenRequest
    ): Result<NotificationResponse> = runCatching {
        postJson<NotificationResponse>(
            baseUrl = appConfiguration.notificationBaseUrl,
            path = "api/v1/tokens",
            bodyObj = notificationTokenRequest,
            headerPairs = listOf(
                "x-api-key" to appConfiguration.notificationApiKey
            ) + appConfiguration.defaultHeaders.toList(),
            successRange = 200..201
        )
    }

    override suspend fun fetchDbIfChanges(
        tokenProvider: IntegrityTokenProvider,
        dbRequest: DbRequest
    ): Result<DbMetaResponse> =
        runCatching {
            val payload =
                """{"productId":"${dbRequest.productId}","appId":"${dbRequest.appId}"}"""
            val integrityToken = tokenProvider.getToken(payload)

            postJson<DbMetaResponse>(
                baseUrl = appConfiguration.dbPlayIntegrityBaseUrl,
                path = "api/v1/db/fetch",
                bodyObj = dbRequest,
                headerPairs = listOf(
                    "x-api-key" to appConfiguration.notificationApiKey,
                    "X-Integrity-Token" to integrityToken
                ) + appConfiguration.defaultHeaders.toList(),
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
