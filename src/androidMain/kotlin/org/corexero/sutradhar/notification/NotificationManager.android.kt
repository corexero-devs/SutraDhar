package org.corexero.sutradhar.notification

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import org.corexero.sutradhar.AndroidAppContext
import org.corexero.sutradhar.Sutradhar

class AndroidNotificationManager(
    private val appContext: Context
) : NotificationManager {

    // Optional: attach an activity when you actually want to prompt
    private var activity: ComponentActivity? = null
    private var launcher: ActivityResultLauncher<String>? = null

    fun attach(activity: ComponentActivity) {
        this.activity = activity
        launcher = activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {

        }
    }

    override fun hasNotificationPermission(): Boolean {
        val enabledGlobally = NotificationManagerCompat.from(appContext).areNotificationsEnabled()
        if (!enabledGlobally) return false
        return if (Build.VERSION.SDK_INT >= 33) {
            ContextCompat.checkSelfPermission(
                appContext, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    /**
     * If permission is already granted (or not needed), returns true.
     * If not granted on Android 13+, this kicks off the system dialog (if an Activity is attached)
     * and returns false immediately. Callers can re-check later via hasNotificationPermission().
     */
    override fun requestPermission(): Boolean {
        if (hasNotificationPermission()) return true
        if (Build.VERSION.SDK_INT >= 33) {
            launcher?.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        return false
    }

    override fun enablePermissionFromSetting() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, appContext.packageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        appContext.startActivity(intent)
    }
}

actual object NotificationFactory {
    actual fun createManager(): NotificationManager =
        AndroidNotificationManager(appContext = (Sutradhar.appContext as AndroidAppContext).get())
}
