package com.example.notes.data.repository

import com.example.notes.data.local.NoteDao
import com.example.notes.data.local.QueueRequestDao
import com.example.notes.data.local.model.Note
import com.example.notes.data.local.model.QueueRequest
import com.example.notes.data.remote.ApiService
import com.example.notes.network.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import com.google.gson.Gson

class NoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val apiService: ApiService,
    private val queueRequestDao: QueueRequestDao,
    @ApplicationContext private val context: Context
) {
    private val gson = Gson()

    suspend fun insert(note: Note) = withContext(Dispatchers.IO) {
        noteDao.insert(note)
        if (NetworkUtils.isNetworkAvailable(context)) {
            syncNotes()
        } else {
            queueRequest("posts", "POST", gson.toJson(note))
        }
    }

    suspend fun update(note: Note) = withContext(Dispatchers.IO) {
        noteDao.update(note)
        if (NetworkUtils.isNetworkAvailable(context)) {
            syncNotes()
        } else {
            queueRequest("posts/${note.id}", "PUT", gson.toJson(note))
        }
    }

    suspend fun delete(note: Note) = withContext(Dispatchers.IO) {
        noteDao.delete(note)
        if (NetworkUtils.isNetworkAvailable(context)) {
            syncNotes()
        } else {
            queueRequest("posts/${note.id}", "DELETE", null)
        }
    }

    fun getNotes(): Flow<List<Note>> = noteDao.getNotes()

    suspend fun getNote(id: Int): Flow<Note> = withContext(Dispatchers.IO) {
        noteDao.getNote(id)
    }

    //synchronizing local data with the remote API. It includes creating, updating, and deleting posts in both local storage and the remote API.
    suspend fun syncNotes() = withContext(Dispatchers.IO) {
        try {
            val localNotes = noteDao.getNotes().first()
            val remoteNotes = apiService.getPosts()

            for (localNote in localNotes) {
                if (remoteNotes.none { it.id == localNote.id }) {
                    apiService.createPost(localNote)
                }
            }

            for (localNote in localNotes) {
                val remoteNote = remoteNotes.find { it.id == localNote.id }
                if (remoteNote != null && (remoteNote.title != localNote.title || remoteNote.text != localNote.text)) {
                    apiService.updatePost(localNote.id, localNote)
                }
            }

            for (remoteNote in remoteNotes) {
                if (localNotes.none { it.id == remoteNote.id }) {
                    apiService.deletePost(remoteNote.id)
                }
            }

            val updatedRemoteNotes = apiService.getPosts()
            noteDao.insertAll(updatedRemoteNotes)
            processQueuedRequests()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Queue requests when offline
    //handles storing requests in the local QueueRequest table when the network is not available.
    private suspend fun queueRequest(endpoint: String, method: String, requestBody: String?) = withContext(Dispatchers.IO) {
        val queueRequest = QueueRequest(endpoint = endpoint, method = method, requestBody = requestBody)
        queueRequestDao.insert(queueRequest)
    }

    //processes the queued requests and executes them when the network is available.
    private suspend fun processQueuedRequests() = withContext(Dispatchers.IO) {
        val queuedRequests = queueRequestDao.getAllRequests().first()
        for (request in queuedRequests) {
            try {

                // Mark request as in-progress
                val inProgressRequest = request.copy(status = "IN_PROGRESS")
                queueRequestDao.insert(inProgressRequest)

                // Process the request based on its method
                when (request.method) {
                    "POST" -> apiService.createPost(gson.fromJson(request.requestBody, Note::class.java))
                    "PUT" -> apiService.updatePost(request.id.toInt(), gson.fromJson(request.requestBody, Note::class.java))
                    "DELETE" -> apiService.deletePost(request.id.toInt())
                }

                // Mark request as completed
                val completedRequest = request.copy(status = "COMPLETED")
                queueRequestDao.insert(completedRequest)

                // delete the completed request
                queueRequestDao.deleteRequest(request.id)
            } catch (e: Exception) {
                e.printStackTrace() // Log the exception for each request
            }
        }
    }
}
