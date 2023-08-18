package com.gordienoye.cellulardatanotifier

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView

class MainActivity : Activity() {
    private var mConnectivityChangeKey: String? = null
    private var mPreferences: SharedPreferences? = null
    private var mRunningImage: ImageView? = null
    private var mRunningText: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
        with (getSharedPreferences(ConnectivityNotifier.PREFERENCES, MODE_PRIVATE)) {
            mConnectivityChangeKey = resources.getString(R.string.pref_key_notify_connectivity_change)
            mRunningText = findViewById<View>(R.id.wifi_notification_change_text) as TextView
            mRunningImage = findViewById<View>(R.id.wifi_notification_change_image) as ImageButton
            this.registerOnSharedPreferenceChangeListener { _: SharedPreferences, key: String? ->
                if (key == mConnectivityChangeKey) {
                    setNotificationState()
                }
            }
            mRunningImage!!.setOnClickListener { _: View? ->
                val wantNotifications = !this.getBoolean(mConnectivityChangeKey, false)
                this.edit().putBoolean(mConnectivityChangeKey, wantNotifications).apply()
                setNotificationState()
            }

            mPreferences = this
        }
        setNotificationState()
    }

    private fun setNotificationState() {
        if (mPreferences!!.getBoolean(mConnectivityChangeKey, false)) {
            mRunningImage!!.setImageResource(R.drawable.notify_changes_on)
            mRunningText!!.text = resources.getString(R.string.listening_for_changes)
        } else {
            mRunningImage!!.setImageResource(R.drawable.notify_changes_off)
            mRunningText!!.text = resources.getString(R.string.ignoring_changes)
        }
        ConnectivityNotifier.updateNotificationState(this)
    }
}