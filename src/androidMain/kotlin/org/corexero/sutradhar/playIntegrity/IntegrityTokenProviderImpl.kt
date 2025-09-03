package org.corexero.sutradhar.playIntegrity

import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.google.android.play.core.integrity.StandardIntegrityManager
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.suspendCancellableCoroutine
import org.corexero.sutradhar.AndroidAppContext
import org.corexero.sutradhar.Sutradhar
import org.corexero.sutradhar.utils.StringExtensions
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class IntegrityTokenProviderImpl : IntegrityTokenProvider {
    private val cloudProjectNumber = 0L
    private var provider: StandardIntegrityManager.StandardIntegrityTokenProvider? = null

    private suspend fun ensureProvider(): StandardIntegrityManager.StandardIntegrityTokenProvider {
        provider?.let { return it }
        val mgr =
            IntegrityManagerFactory.createStandard((Sutradhar.appContext as AndroidAppContext).get())
        val req = StandardIntegrityManager.PrepareIntegrityTokenRequest.builder()
            .setCloudProjectNumber(cloudProjectNumber).build()
        val prov =
            suspendCancellableCoroutine<StandardIntegrityManager.StandardIntegrityTokenProvider> { cont ->
                mgr.prepareIntegrityToken(req).addOnSuccessListener { cont.resume(it) }
                    .addOnFailureListener { cont.resumeWithException(it) }
            }
        provider = prov
        return prov
    }

    override suspend fun getToken(requestHashB64Url: String): String {
        val requestHashB64Url =
            StringExtensions().requestHashBase64Url(requestHashB64Url.toByteArray())
        val prov = ensureProvider()
        val tokenReq = StandardIntegrityManager.StandardIntegrityTokenRequest.builder()
            .setRequestHash(requestHashB64Url).build()
        return suspendCancellableCoroutine { cont ->
            prov.request(tokenReq).addOnSuccessListener { cont.resume(it.token()) }
                .addOnFailureListener { cont.resumeWithException(it) }
        }
    }
}

