package com.serapercel.trickle.data.repository

import com.serapercel.trickle.data.entity.Need
import com.serapercel.trickle.data.entity.User
import com.serapercel.trickle.data.source.local.LocalDataSource
import com.serapercel.trickle.data.source.remote.RemoteDataSource
import com.serapercel.trickle.domain.repository.NeedsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NeedsRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : NeedsRepository {
    override suspend fun getNeeds(user: User): List<Need> = remoteDataSource.getNeeds(user)

    override suspend fun addNeed(need: Need, user: User): Boolean =
        remoteDataSource.addNeed(need, user)

    override suspend fun deleteNeed(need: Need, user: User): Boolean =
        remoteDataSource.deleteNeed(need, user)

    override fun readDatabase(): Flow<List<Need>> = localDataSource.readDatabase()

    override suspend fun insertNeeds(need: Need) = localDataSource.insertNeeds(need)

    override suspend fun insertAllNeeds(needList: List<Need>) =
        localDataSource.insertAllNeeds(needList)

    override suspend fun deleteNeed(need: Need) = localDataSource.deleteNeed(need)

    override suspend fun deleteAllNeed() = localDataSource.deleteAllNeed()

}