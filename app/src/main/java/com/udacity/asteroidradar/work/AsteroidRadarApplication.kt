package com.udacity.asteroidradar.work

import android.app.Application
import android.os.Build
import androidx.work.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

//The application will run everytime the application launched
class AsteroidRadarApplication : Application() {

    //Create Scope for application
    val applicationScope = CoroutineScope(Dispatchers.Default)


    //   Notes - It's important to note that WorkManager.initialize should be called from inside onCreate without using a background
    //   thread to avoid issues caused when initialization happens after WorkManager is used.
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        delayedInit()
    }


    private fun delayedInit() = applicationScope.launch {
        setUpRecurringWork()
    }

    private fun setUpRecurringWork() {

        //Define Contstaints for workManager to run when the device has to be connected
        // to wifi, or the device is charging
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }


        val repeatinRequest =
            PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS).build()

        //Schedule the work as unique
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatinRequest
        )
    }
}