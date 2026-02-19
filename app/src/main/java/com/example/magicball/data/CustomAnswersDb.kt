package com.example.magicball.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "custom_answers")
data class CustomAnswersDb(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String
)
