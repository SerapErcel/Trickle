package com.serapercel.trickle.data.repository

import com.serapercel.trickle.data.entity.ITransaction
import com.serapercel.trickle.data.entity.User
import com.serapercel.trickle.data.source.local.LocalDataSource
import com.serapercel.trickle.data.source.remote.RemoteDataSource
import com.serapercel.trickle.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource

) : TransactionRepository {

    override suspend fun getTransactions(user: User): List<ITransaction> =
        remoteDataSource.getTransactions(user)


    override suspend fun addTransaction(transaction: ITransaction, user: User) =
        remoteDataSource.addTransaction(transaction, user)


    override suspend fun deleteTransaction(transaction: ITransaction, user: User) =
        remoteDataSource.deleteTransaction(transaction, user)

    override suspend fun deleteAllTransactions(user: User) =
        remoteDataSource.deleteAllTransactions(user)

    override fun readTransactionsDb(): Flow<List<ITransaction>> {
        TODO("Not yet implemented")
    }
}