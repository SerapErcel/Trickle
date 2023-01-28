package com.serapercel.trickle.data.repository

import com.serapercel.trickle.data.entity.Need
import com.serapercel.trickle.data.source.local.LocalNeedsDataSource
import com.serapercel.trickle.domain.repository.NeedsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NeedsRepositoryImpl @Inject constructor(
    private val localNeedsDataSource: LocalNeedsDataSource
) : NeedsRepository {
    override fun readDatabase(): Flow<List<Need>> = localNeedsDataSource.readDatabase()


    override suspend fun insertNeeds(need: Need) = localNeedsDataSource.insertNeeds(need)

    override suspend fun deleteNeed(need: Need) = localNeedsDataSource.deleteNeed(need)

    override suspend fun updateNeed(id: Int?, name: String?, count: String?) {
        localNeedsDataSource.updateNeed(id, name, count)
    }
}