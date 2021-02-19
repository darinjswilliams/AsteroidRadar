package com.udacity.asteroidradar.logger

import android.app.Application
import timber.log.Timber

class LogUtilsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}