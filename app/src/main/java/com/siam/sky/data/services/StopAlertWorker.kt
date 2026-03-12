package com.siam.sky.data.services

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.siam.sky.core.db.WeatherDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StopAlertWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        AlertManager.stopAlarm(applicationContext)
        
        val alertId = inputData.getString("ALERT_ID")
        if (alertId != null) {
            withContext(Dispatchers.IO) {
                val alertDao = WeatherDataBase.getInstance(applicationContext).getAlertDao()
                alertDao.deleteAlertById(alertId)
            }
        }
        
        return Result.success()
    }
}