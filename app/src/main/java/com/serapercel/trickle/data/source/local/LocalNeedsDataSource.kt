package com.serapercel.trickle.data.source.local

import com.serapercel.trickle.data.entity.Need
import kotlinx.coroutines.flow.Flow

interface LocalNeedsDataSource {

    fun readDatabase(): Flow<List<Need>>

    suspend fun insertNeeds(need: Need)

    suspend fun insertAllNeeds(needList: List<Need>)

    suspend fun deleteNeed(need: Need)

    suspend fun deleteAllNeed()

    suspend fun updateNeed(id: Int?, name: String?, count: String?)
}