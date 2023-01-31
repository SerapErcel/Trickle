package com.serapercel.trickle.domain.repository

import com.serapercel.trickle.data.entity.Need
import com.serapercel.trickle.data.entity.User
import kotlinx.coroutines.flow.Flow

interface NeedsRepository {

    suspend fun getNeeds(user: User) : List<Need>

    fun readDatabase(): Flow<List<Need>>

    suspend fun insertNeeds(need: Need)

    suspend fun insertAllNeeds(needList: List<Need>)

    suspend fun deleteNeed(need: Need)

    suspend fun deleteAllNeed()

    suspend fun updateNeed(id: Int?, name: String?, count: String?)
}