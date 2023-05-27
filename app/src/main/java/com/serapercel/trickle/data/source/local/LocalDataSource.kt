package com.serapercel.trickle.data.source.local

import com.serapercel.trickle.data.entity.Need
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    /** Needs **/

    fun readDatabase(): Flow<List<Need>>

    suspend fun insertNeeds(need: Need)

    suspend fun insertAllNeeds(needList: List<Need>)

    suspend fun deleteNeed(need: Need)

    suspend fun deleteAllNeed()

    suspend fun updateNeed(id: Int?, name: String?, count: String?)


    /** Transactions **/

   // suspend fun getTransactions(user: User): Flow<List<ITransaction>>

    //suspend fun addTransaction(transaction: ITransaction, user: User)

   // suspend fun deleteTransaction(transaction: ITransaction, user: User)

   // fun readTransactionsDb(): Flow<List<ITransaction>>
}