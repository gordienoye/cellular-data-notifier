package com.gordienoye.cellulardatanotifier

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.gordienoye.cellulardatanotifier.ConnectivityNotifier.updateNotificationState

class ConnectivityBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        updateNotificationState(context)
    }
}