package org.corexero.sutradhar.notification



interface NotificationManager {
    fun hasNotificationPermission(): Boolean
    fun requestPermission(): Boolean
    fun enablePermissionFromSetting()
}

expect object NotificationFactory {
    fun createManager(): NotificationManager
}
