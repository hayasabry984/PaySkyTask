package com.example.notes.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.notes.data.repository.NoteRepository
import com.example.notes.network.NetworkUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val noteRepository: NoteRepository
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            if (NetworkUtils.isNetworkAvailable(context)) {
                noteRepository.syncNotes()
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
