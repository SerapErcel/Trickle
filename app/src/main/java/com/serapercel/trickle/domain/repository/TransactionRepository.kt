package com.serapercel.trickle.domain.repository

import com.serapercel.trickle.data.entity.Account
import com.serapercel.trickle.data.entity.ITransaction
import com.serapercel.trickle.data.entity.User
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    suspend fun getTransactions(account: Account): List<ITransaction>

    suspend fun getAllTransactions(user: User): List<ITransaction>

    suspend fun addTransaction(transaction: ITransaction, user: User): Boolean

    suspend fun deleteTransaction(transaction: ITransaction, user: User): Boolean

    suspend fun deleteAllTransactions(user: User)

    fun readTransactionsDb(): Flow<List<ITransaction>>
}