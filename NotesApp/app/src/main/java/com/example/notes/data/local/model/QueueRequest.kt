package com.example.notes.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

@Entity(tableName = "queue_request")
data class QueueRequest(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "endpoint")
    val endpoint: String,

    @ColumnInfo(name = "method")
    val method: String,

    @ColumnInfo(name = "request_body")
    val requestBody: String?,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "status")
    val status: String = "PENDING"
) {
    companion object {
        fun fromJson(json: String): QueueRequest {
            return Gson().fromJson(json, QueueRequest::class.java)
        }

        fun toJson(queueRequest: QueueRequest): String {
            return Gson().toJson(queueRequest)
        }
    }
}
