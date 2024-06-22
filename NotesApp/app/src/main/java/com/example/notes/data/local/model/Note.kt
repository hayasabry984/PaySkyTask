package com.example.notes.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "text")
    var text: String,
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }

    companion object {
        fun fromString(json: String): Note {
            return Gson().fromJson(json, Note::class.java)
        }
    }
}
