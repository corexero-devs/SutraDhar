package org.corexero.sutradhar.notification

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
private const val CHANNEL_ID = "default_chanel"
private const val DEFAULT_NOTIFICATION_ID = 1000
const val CHANNEL_NAME = "General Notifications"
const val CHANNEL_DESCRIPTION =
    "Notifications about updates, alerts, and other important information."

@SuppressLint("MissingPermission")
fun Context.showNotification(
    textTitle: String?,
    textContent: String?,
    id: Int = DEFAULT_NOTIFICATION_ID
) {
    val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            //.setSmallIcon(Ima)
        .setContentTitle(textTitle)
        .setContentText(textContent)
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