package com.serapercel.trickle.data.source.database

import androidx.room.*
import com.serapercel.trickle.data.entity.Need
import kotlinx.coroutines.flow.Flow

@Dao
interface NeedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(need: Need)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(needList: List<Need>)

    @Delete
    suspend fun delete(need: Need)

    @Query("DELETE from needs_table")
    suspend fun deleteAllNeed()

    @Query("SELECT * from needs_table order by id ASC")
    fun getAllNeeds(): Flow<List<Need>>

    @Query("UPDATE needs_table Set name= :name, count= :count WHERE id= :id")
    suspend fun update(id: Int?, name: String?, count: String?)
}