package com.example.notes.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.notes.data.local.model.Note
import com.example.notes.data.local.model.QueueRequest

@Database(entities = [Note::class, QueueRequest::class], version = 3, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun queueRequestDao(): QueueRequestDao
}
