package es.dam.booknest.model

import kotlinx.serialization.Serializable

@Serializable
data class UserStreak(
    val streakDays: Int,
    val lastReadingDate: String?
)