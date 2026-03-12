package com.siam.sky.data.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class DismissAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        AlertManager.stopAlarm(context)
    }
}
