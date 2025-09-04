package org.corexero.sutradhar.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

private const val CHANNEL_ID = "default_chanel"
private const val DEFAULT_NOTIFICATION_ID = 1000
const val CHANNEL_NAME = "General Notifications"
const val CHANNEL_DESCRIPTION =
    "Notifications about updates, alerts, and other important information."

const val DEEPLINK_NAVIGATION_URL = "deeplink_navigation_url"

@SuppressLint("MissingPermission")
fun Context.showNotification(
    textTitle: String?,
    textContent: String?,
    appIconResId: Int,
    action: String?,
    id: Int = DEFAULT_NOTIFICATION_ID
) {

    val launchIntent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        action?.let { putExtra(DEEPLINK_NAVIGATION_URL, it) }
    }

    val pendingIntent: PendingIntent? = launchIntent?.let {
        PendingIntent.getActivity(
            this, 0, it,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(appIconResId)
        .setContentTitle(textTitle)
        .setContentText(textContent)
        .setContentIntent(pendingIntent)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    with(NotificationManagerCompat.from(this)) {
        if (ActivityCompat.checkSelfPermission(
                this@showNotification,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return@with
        }
        notify(id, builder.build())
    }
}

fun Context.createDefaultNotificationChannel() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            description = CHANNEL_DESCRIPTION
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}