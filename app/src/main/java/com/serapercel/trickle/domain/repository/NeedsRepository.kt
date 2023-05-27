package com.serapercel.trickle.domain.repository

import com.serapercel.trickle.data.entity.Need
import com.serapercel.trickle.data.entity.User
import kotlinx.coroutines.flow.Flow

interface NeedsRepository {

    suspend fun getNeeds(user: User): List<Need>

    suspend fun addNeed(need: Need, user: User): Boolean

    suspend fun deleteNeed(need: Need, user: User): Boolean

    fun readDatabase(): Flow<List<Need>>

    suspend fun insertNeeds(need: Need)

    suspend fun insertAllNeeds(needList: List<Need>)

    suspend fun deleteNeed(need: Need)

    suspend fun deleteAllNeed()

}