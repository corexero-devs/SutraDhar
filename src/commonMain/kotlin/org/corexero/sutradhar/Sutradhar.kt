package org.corexero.sutradhar

import io.ktor.client.HttpClient
import org.corexero.sutradhar.network.HttpClientProvider
import org.corexero.sutradhar.network.NetworkConfig

/** App supplies this at runtime. Keep secrets out of the library. */
interface SutradharConfig {
    val reviewBaseUrl: String
        get() = NetworkConfig.REVIEW_BASE_URL

    val notificationBaseUrl: String
        get() = NetworkConfig.NOTIFICATION_BASE_URL

    fun reviewApiKey(): String
    fun notificationApiKey(): String
    val defaultHeaders: Map<String, String> get() = emptyMap()
}

object Sutradhar {
    lateinit var config: SutradharConfig
        private set

    lateinit var httpClient: HttpClient
        private set

    lateinit var appContext: AppContext
        private set

    fun init(
        config: SutradharConfig,
        httpClient: HttpClient = HttpClientProvider.client,
        appContext: AppContext
    ) {
        this.config = config
        this.httpClient = httpClient
        this.appContext = appContext
    }

    val isReady: Boolean
        get() = this::config.isInitialized && this::httpClient.isInitialized
}
