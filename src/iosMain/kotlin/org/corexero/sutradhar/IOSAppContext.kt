package org.corexero.sutradhar

import platform.UserNotifications.UNUserNotificationCenter

class IOSAppContext(
    val notificationCenter: UNUserNotificationCenter =
        UNUserNotificationCenter.currentNotificationCenter()
) : AppContext
