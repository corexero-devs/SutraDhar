package org.corexero.sutradhar.notification

class IOSNotificationManager : NotificationManager {
    override fun hasNotificationPermission() = true
    override fun requestPermission() = true
    override fun enablePermissionFromSetting() {}
}