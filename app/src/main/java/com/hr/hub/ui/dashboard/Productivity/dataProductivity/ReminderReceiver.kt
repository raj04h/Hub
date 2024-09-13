package com.hr.hub.ui.dashboard.Productivity.dataProductivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) {
            Log.e("ReminderReceiver", "Context or intent is null")
            return
        }

        val label = intent.getStringExtra("Alarm_Label")
        val uriString = intent.getStringExtra("Alarm_Ringtone_Uri")

        if (uriString.isNullOrEmpty()) {
            Log.e("ReminderReceiver", "Ringtone URI is null")
            return
        }

        val ringtoneUri = Uri.parse(uriString)
        val ringtone: Ringtone = RingtoneManager.getRingtone(context, ringtoneUri)
        ringtone.play()

        Log.d("ReminderReceiver", "Playing ringtone: $uriString with label: $label")
    }
}
