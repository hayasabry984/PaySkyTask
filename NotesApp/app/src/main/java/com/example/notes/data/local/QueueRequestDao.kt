package com.example.notes.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.notes.data.local.model.QueueRequest
import kotlinx.coroutines.flow.Flow

@Dao
interface QueueRequestDao {
    @Insert
    suspend fun insert(queueRequest: QueueRequest)

    @Query("SELECT * FROM queue_request")
    fun getAllRequests(): Flow<List<QueueRequest>>

    @Query("DELETE FROM queue_request WHERE id = :id")
    suspend fun deleteRequest(id: Long)
}
