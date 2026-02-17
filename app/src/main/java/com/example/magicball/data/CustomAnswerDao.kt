package com.example.magicball.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CustomAnswerDao {
    @Query("SELECT * FROM custom_answers ORDER BY id DESC")
    suspend fun getAll(): List<CustomAnswer>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CustomAnswer)

    @Query("DELETE FROM custom_answers")
    suspend fun clear()
}
