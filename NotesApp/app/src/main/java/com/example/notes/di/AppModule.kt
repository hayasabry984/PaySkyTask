package com.example.notes.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton
import com.example.notes.data.repository.NoteRepository
import com.example.notes.data.remote.ApiService
import com.example.notes.data.local.QueueRequestDao
import com.example.notes.data.local.NoteDao

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext app: Context): Context = app

    @Provides
    @Singleton
    fun provideNoteRepository(
        noteDao: NoteDao,
        apiService: ApiService,
        queueRequestDao: QueueRequestDao,
        @ApplicationContext context: Context
    ): NoteRepository {
        return NoteRepository(noteDao, apiService, queueRequestDao, context)
    }
}
