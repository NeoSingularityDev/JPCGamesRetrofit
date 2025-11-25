package com.example.gamesretrofit.data

data class DeveloperDTO(
    val id: Long,
    val name: String,
    val country: String?
)

data class GameDTO(
    val id: Long,
    val title: String,
    val description: String?,
    val year: Int?,
    val developer: DeveloperDTO?
)

data class GameRequest(
    val title: String,
    val description: String? = null,
    val year: Int? = null,
    val developer_id: Long
)

data class DeveloperRequest(
    val name: String,
    val country: String? = null
)
