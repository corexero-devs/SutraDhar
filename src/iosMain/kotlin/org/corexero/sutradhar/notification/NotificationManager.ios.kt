package org.corexero.sutradhar.notification

private class IOSNotificationManager : NotificationManager {
    override fun hasNotificationPermission() = true
    override fun requestPermission() = true
    override fun enablePermissionFromSetting() {}
}

actual object NotificationFactory {
    actual fun createManager(): NotificationManager = IOSNotificationManager()
}
