package com.example.notes.workers

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.notes.data.repository.NoteRepository
import javax.inject.Inject

class SyncWorkerFactory @Inject constructor(
    private val hiltWorkerFactory: HiltWorkerFactory,
    private val noteRepository: NoteRepository
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            SyncWorker::class.java.name -> SyncWorker(appContext, workerParameters, noteRepository)
            else -> hiltWorkerFactory.createWorker(appContext, workerClassName, workerParameters)
        }
    }
}
