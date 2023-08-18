package com.gordienoye.cellulardatanotifier

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import com.gordienoye.cellulardatanotifier.R

object ConnectivityNotifier {
    val PREFERENCES = ConnectivityNotifier::class.java.name + "_PREFERENCES"
    fun updateNotificationState(context: Context) {
        val WIFI_NOTIFICATION_ID = -0x21524111 // 0xDEADBEEF
        val VIBRATION_PATTERN = longArrayOf(1000, 1000, 1000, 1000)
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Get the value of the shared preference to determine if we are interested in being
        // notified of changes in connectivity.
        val prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
        val wantNotifications = prefs.getBoolean(
            context.resources.getString(R.string.pref_key_notify_connectivity_change),
            false
        )
        val visiblePref = context.resources.getString(R.string.pref_key_notification_visible)
        val notificationVisible = prefs.getBoolean(visiblePref, false)
        val showNotification = wantNotifications && Connectivity.isConnectedMobile(context)
        if (showNotification) {
            // do not redisplay the notification if it is already shown.
            if (notificationVisible) {
                return
            }

            // store the current state of the notification visibility
            prefs.edit().putBoolean(visiblePref, true).apply()

            // If we want notifications and we are currently using mobile data then put up a forceful
            // notification indicating that fact.
            val appIntent = Intent(context, MainActivity::class.java)
            appIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val intent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_IMMUTABLE)
            val notification: Notification = Notification.Builder(context)
                .setSmallIcon(R.drawable.notification_icon)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.drawable.notify_changes_on
                    )
                )
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_text))
                .setPriority(Notification.PRIORITY_HIGH)
                .setVibrate(VIBRATION_PATTERN)
                .setColor(Color.RED)
                .setOngoing(true)
                .setContentIntent(intent)
                .build()
            nm.notify(WIFI_NOTIFICATION_ID, notification)
        } else {
            // store the current state of the notification visibility
            prefs.edit().putBoolean(visiblePref, false).apply()

            // If we are updating the notifier because we no longer want to show notifications OR if
            // we are not connected to mobile data then remove the (potentially) existing notification.
            nm.cancel(WIFI_NOTIFICATION_ID)
        }
    }
}