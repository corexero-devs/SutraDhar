package org.corexero.sutradhar.notification


import android.content.pm.PackageManager
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.corexero.sutradhar.network.HttpClientProvider
import org.corexero.sutradhar.network.SutradharRepositoryImpl
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Locale

class MyFirebaseMessagingService : FirebaseMessagingService(), KoinComponent {

    private val httpClient: HttpClient by this.inject()
    private val repo = SutradharRepositoryImpl(httpClient)
    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        // Ensure we have a token at boot/install/update
//        FirebaseMessaging.getInstance().token.addOnSuccessListener { t ->
//            if (!t.isNullOrBlank()) sendToken(t)
//        }
    }

    override fun onNewToken(token: String) {
        Log.d("FCM", "New FCM token: $token")
        sendToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        message.notification?.let {
            showNotification(it.title, it.body)
        }
        Log.d("FCM", "Message received: ${message.data}")
    }

    private fun sendToken(token: String) {
        val req = NotificationTokenRequest(
            token = token,
            platform = "android",
            productId = "BuildConfig.PRODUCT_ID",     // define in app’s build.gradle
            appId = packageName,
            userIdentifier = null,                  // fill if you have a logged-in user id
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
