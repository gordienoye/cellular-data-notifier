package com.gordienoye.cellulardatanotifier;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends Activity {

  private String mConnectivityChangeKey;
  private SharedPreferences mPreferences;
  private ImageView mRunningImage;
  private TextView mRunningText;


  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.main);

    mConnectivityChangeKey = getResources().getString(R.string.pref_key_notify_connectivity_change);
    mPreferences = getSharedPreferences(ConnectivityNotifier.PREFERENCES, Context.MODE_PRIVATE);
    mRunningText = (TextView) findViewById(R.id.wifi_notifiaction_change_text);
    mRunningImage = (ImageButton) findViewById(R.id.wifi_notifiaction_change_image);

    mPreferences.registerOnSharedPreferenceChangeListener((prefs, key) -> {
      if (key.equals(mConnectivityChangeKey)) {
        setNotificationState();
      }
    });

    mRunningImage.setOnClickListener(v -> {
      boolean wantNotifications = !mPreferences.getBoolean(mConnectivityChangeKey, false);
      mPreferences.edit().putBoolean(mConnectivityChangeKey, wantNotifications).commit();
      setNotificationState();
    });

    setNotificationState();
  }

  private void setNotificationState () {
    if (mPreferences.getBoolean(mConnectivityChangeKey, false)) {
      mRunningImage.setImageResource(R.drawable.notify_changes_on);
      mRunningText.setText(getResources().getString(R.string.listening_for_changes));

    } else {
      mRunningImage.setImageResource(R.drawable.notify_changes_off);
      mRunningText.setText(getResources().getString(R.string.ignoring_changes));
    }

    ConnectivityNotifier.updateNotificationState(this);
  }
}
