package org.corexero.sutradhar.notification


import android.content.pm.PackageManager
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.corexero.sutradhar.Sutradhar
import org.corexero.sutradhar.network.repository.SutradharRepository
import org.corexero.sutradhar.notification.dto.NotificationTokenRequest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Locale

class MyFirebaseMessagingService : FirebaseMessagingService(), KoinComponent {

    private val repo by inject<SutradharRepository>()

    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        sendToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        message.notification?.let {
            showNotification(it.title, it.body)
        }
    }

    private fun sendToken(token: String) {
        val req = NotificationTokenRequest(
            token = token,
            platform = "android",
            productId = Sutradhar.config.productId,
            appId = packageName,
            userIdentifier = null,
            locale = Locale.getDefault().toLanguageTag(),
            appVersion = appVersion()
        )
        ioScope.launch {
            val result = repo.saveUserFCMToken(req)
            result.onSuccess { Log.d("FCM", "Token stored OK: $it") }
                .onFailure { Log.e("FCM", "Token store failed", it) }
        }
    }

    private fun appVersion(): String {
        return try {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            pInfo.versionName ?: "unknown"
        } catch (e: PackageManager.NameNotFoundException) {
            "unknown"
        }
    }
}
