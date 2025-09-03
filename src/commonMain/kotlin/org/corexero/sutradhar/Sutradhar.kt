package org.corexero.sutradhar

import org.corexero.sutradhar.network.NetworkConfig

/** App supplies this at runtime. Keep secrets out of the library. */
interface SutradharConfig {
    val productId: String



    val notificationIcon: Int

    val defaultHeaders: Map<String, String> get() = emptyMap()
}

object Sutradhar {

    lateinit var config: SutradharConfig
        private set

    lateinit var appContext: AppContext
        private set

    fun init(
        config: SutradharConfig,
        appContext: AppContext
    ) {
        this.config = config
        this.appContext = appContext
    }

}
