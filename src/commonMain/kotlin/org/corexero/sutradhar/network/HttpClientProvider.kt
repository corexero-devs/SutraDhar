package org.corexero.sutradhar.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientProvider {
    val jsonConfig = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    }

    val client: HttpClient by lazy {
        HttpClient {
            install(ContentNegotiation) { json(jsonConfig) }
            install(Logging) { level = LogLevel.ALL }
            install(HttpTimeout) {
                requestTimeoutMillis = 15_000
                connectTimeoutMillis = 10_000
                socketTimeoutMillis = 15_000
            }
            expectSuccess = false
        }
    }
}
