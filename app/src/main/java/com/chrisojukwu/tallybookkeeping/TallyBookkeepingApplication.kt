package com.chrisojukwu.tallybookkeeping

import android.app.Application
import android.os.Build
import android.os.StrictMode
import androidx.annotation.RequiresApi
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TallyBookkeepingApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}