package com.example.magicball.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CustomAnswerDao {

    @Query("SELECT * FROM custom_answers ORDER BY id DESC")
    suspend fun getAll(): List<CustomAnswersDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CustomAnswersDb)

    @Delete
    suspend fun delete(item: CustomAnswersDb)
}
