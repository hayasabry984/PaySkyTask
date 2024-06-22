package com.example.notes.data.remote

import com.example.notes.data.local.model.Note
import retrofit2.http.*

interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Note>

    @POST("posts")
    suspend fun createPost(@Body note: Note): Note

    @PUT("posts/{id}")
    suspend fun updatePost(@Path("id") id: Int, @Body note: Note): Note

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Int)
}
