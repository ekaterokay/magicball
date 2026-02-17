package com.example.magicball.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_answers")
data class CustomAnswerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val text: String
)
