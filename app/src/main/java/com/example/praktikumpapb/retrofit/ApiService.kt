package com.example.praktikumpapb.retrofit
import com.example.praktikumpapb.entity.Github
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): Github
}