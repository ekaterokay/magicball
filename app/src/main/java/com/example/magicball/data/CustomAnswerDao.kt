package com.example.magicball.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomAnswerDao {

    @Query("SELECT * FROM custom_answers ORDER BY id DESC")
    fun getAll(): Flow<List<CustomAnswerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CustomAnswerEntity)

    @Delete
    suspend fun delete(item: CustomAnswerEntity)
}
