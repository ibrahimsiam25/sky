package com.siam.sky.data.datasources.local.imp

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.siam.sky.data.datasources.local.dao.AlertDao
import com.siam.sky.data.datasources.local.AlertLocalDataSource
import com.siam.sky.data.models.AlertModel
import com.siam.sky.data.services.StopAlertWorker
import com.siam.sky.data.services.WeatherAlertWorker
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

class AlertLocalDataSourceImp(
    private val workManager: WorkManager,
    private val alertDao: AlertDao
) : AlertLocalDataSource {

    override fun scheduleAlert(alert: AlertModel) {
        val currentTime = System.currentTimeMillis()
        val startDelay = (alert.startTime - currentTime).coerceAtLeast(0L)
        val endDelay = (alert.endTime - currentTime).coerceAtLeast(0L)
        val tag = "alert_${alert.id}"

        val startWork = OneTimeWorkRequestBuilder<WeatherAlertWorker>()
            .setInitialDelay(startDelay, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf("ALERT_TYPE" to alert.type.name, "ALERT_ID" to alert.id))
            .addTag(tag)
            .build()

        val stopWork = OneTimeWorkRequestBuilder<StopAlertWorker>()
            .setInitialDelay(endDelay, TimeUnit.MILLISECONDS)
            .setInputData(workDataOf("ALERT_ID" to alert.id))
            .addTag(tag)
            .build()

        workManager.enqueue(listOf(startWork, stopWork))
    }

    override fun cancelAlert(alert: AlertModel) {
        workManager.cancelAllWorkByTag("alert_${alert.id}")
    }

    override suspend fun insertAlert(alert: AlertModel) {
        alertDao.insertAlert(alert)
    }

    override suspend fun deleteAlert(alert: AlertModel) {
        alertDao.deleteAlert(alert)
    }

    override suspend fun updateAlert(alert: AlertModel) {
        alertDao.updateAlert(alert)
    }

    override fun getAllAlerts(): Flow<List<AlertModel>> {
        return alertDao.getAllAlerts()
    }
}
