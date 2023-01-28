package com.serapercel.trickle.domain.repository

import com.serapercel.trickle.data.entity.Need
import kotlinx.coroutines.flow.Flow

interface NeedsRepository {

    fun readDatabase(): Flow<List<Need>>

    suspend fun insertNeeds(need: Need)

    suspend fun deleteNeed(need: Need)

    suspend fun updateNeed(id: Int?, name: String?, count: String?)
}