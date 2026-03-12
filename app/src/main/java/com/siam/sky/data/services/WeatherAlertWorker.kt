package com.siam.sky.data.services


import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.siam.sky.data.models.AlertType


class WeatherAlertWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val typeStr = inputData.getString("ALERT_TYPE") ?: "NOTIFICATION"
        val alertType = AlertType.valueOf(typeStr)

        when (alertType) {
            AlertType.NOTIFICATION -> AlertManager.showNotification(
                applicationContext,
                "Weather Alert",
                "Weather conditions require your attention"
            )
            AlertType.ALARM -> AlertManager.startAlarm(applicationContext)
        }

        return Result.success()
    }
}