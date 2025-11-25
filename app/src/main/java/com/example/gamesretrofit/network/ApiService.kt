package com.example.gamesretrofit.network

import com.example.gamesretrofit.data.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("v1/games")
    fun getGames(): Call<List<GameDTO>>

    @GET("v1/games/{id}")
    fun getGame(@Path("id") id: Long): Call<GameDTO>

    @POST("v1/games")
    fun createGame(@Body body: GameRequest): Call<GameDTO>

    @PUT("v1/games/{id}")
    fun updateGame(@Path("id") id: Long, @Body body: GameRequest): Call<GameDTO>

    @DELETE("v1/games/{id}")
    fun deleteGame(@Path("id") id: Long): Call<Map<String, String>>

    @GET("v1/developers")
    fun getDevelopers(): Call<List<DeveloperDTO>>

    @POST("v1/developers")
    fun createDeveloper(@Body body: DeveloperRequest): Call<DeveloperDTO>

    @PUT("v1/developers/{id}")
    fun updateDeveloper(@Path("id") id: Long, @Body body: DeveloperRequest): Call<DeveloperDTO>

    @DELETE("v1/developers/{id}")
    fun deleteDeveloper(@Path("id") id: Long): Call<Map<String, String>>
}
