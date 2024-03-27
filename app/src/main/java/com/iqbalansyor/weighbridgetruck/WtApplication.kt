package com.iqbalansyor.weighbridgetruck

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WtApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)
    }

    companion object {
        lateinit var INSTANCE: WtApplication
            private set
        private const val APP_NAME = "wt"
    }
}