package com.gordienoye.cellulardatanotifier;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class ConnectivityBroadcastReceiver extends BroadcastReceiver {

  @Override
  public void onReceive (Context context, Intent intent) {
    ConnectivityNotifier.updateNotificationState(context);
  }
}