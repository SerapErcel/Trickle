package com.serapercel.trickle.data.repository

import com.serapercel.trickle.data.entity.Account
import com.serapercel.trickle.data.entity.ITransaction
import com.serapercel.trickle.data.entity.User
import com.serapercel.trickle.data.source.remote.RemoteDataSource
import com.serapercel.trickle.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource

) : TransactionRepository {

    override suspend fun getTransactions(account: Account): List<ITransaction> =
        remoteDataSource.getTransactions(account)

    override suspend fun getAllTransactions(user: User): List<ITransaction> =
        remoteDataSource.getAllTransactions(user)

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