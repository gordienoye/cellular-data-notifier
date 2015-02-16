package com.gordienoye.cellulardatanotifier;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class ConnectivityNotifier {
  public static final String PREFERENCES = ConnectivityNotifier.class.getName() + "_PREFERENCES";


  static public void updateNotificationState (Context context) {
    final int WIFI_NOTIFICATION_ID = 0xDEADBEEF;
    final long[] VIBRATION_PATTERN = new long[]{1000, 1000, 1000, 1000};

    NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

    // Get the value of the shared preference to determine if we are interested in being
    // notified of changes in connectivity.
    SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    boolean wantNotifications = prefs.getBoolean(
        context.getResources().getString(R.string.pref_key_notify_connectivity_change),
        false
    );
    String visiblePref = context.getResources().getString(R.string.pref_key_notification_visible);
    boolean notificationVisible = prefs.getBoolean(visiblePref, false);

    boolean showNotification = wantNotifications && Connectivity.isConnectedMobile(context);

    if (showNotification) {
      // do not redisplay the notification if it is already shown.
      if (notificationVisible) {
        return;
      }

      // store the current state of the notification visibility
      prefs.edit().putBoolean(visiblePref, true).apply();

      // If we want notifications and we are currently using mobile data then put up a forceful
      // notification indicating that fact.
      Intent appIntent = new Intent(context, MainActivity.class);
      appIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
      PendingIntent intent = PendingIntent.getActivity(context, 0, appIntent, 0);

      Notification notification =
          new Notification.Builder(context)
              .setSmallIcon(R.drawable.notification_icon)
              .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.notify_changes_on))
              .setContentTitle(context.getString(R.string.notification_title))
              .setContentText(context.getString(R.string.notification_text))
              .setPriority(Notification.PRIORITY_HIGH)
              .setVibrate(VIBRATION_PATTERN)
              .setColor(Color.RED)
              .setOngoing(true)
              .setContentIntent(intent)
              .build();

      nm.notify(WIFI_NOTIFICATION_ID, notification);

    } else {
      // store the current state of the notification visibility
      prefs.edit().putBoolean(visiblePref, false).apply();

      // If we are updating the notifier because we no longer want to show notifications OR if
      // we are not connected to mobile data then remove the (potentially) existing notification.
      nm.cancel(WIFI_NOTIFICATION_ID);
    }
  }
}
