package com.serapercel.trickle.domain.repository

import com.serapercel.trickle.data.entity.ITransaction
import com.serapercel.trickle.data.entity.User
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    suspend fun getTransactions(user: User): Flow<List<ITransaction>>

    suspend fun addTransaction(transaction: ITransaction, user: User)

    suspend fun deleteTransaction(transaction: ITransaction, user: User)

    suspend fun deleteAllTransactions(user: User)

    fun readTransactionsDb(): Flow<List<ITransaction>>
}