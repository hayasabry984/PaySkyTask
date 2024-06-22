package com.example.notes

import android.app.Application
import androidx.work.Configuration
import com.example.notes.workers.SyncWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class NoteApp : Application(), Configuration.Provider {

    @Inject
    lateinit var syncWorkerFactory: SyncWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(syncWorkerFactory)
            .build()
    }
}
