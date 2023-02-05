package com.serapercel.trickle.data.source.remote

import com.serapercel.trickle.data.entity.ITransaction
import com.serapercel.trickle.data.entity.Need
import com.serapercel.trickle.data.entity.User
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    /** Needs **/
    suspend fun getNeeds(user: User): List<Need>
    suspend fun addNeed(need: Need, user: User): Boolean
    suspend fun deleteNeed(need: Need, user: User): Boolean

    /** Transactions **/

    suspend fun getTransactions(user: User): Flow<List<ITransaction>>

    suspend fun addTransaction(transaction: ITransaction, user: User)

    suspend fun deleteTransaction(transaction: ITransaction, user: User)

    suspend fun deleteAllTransactions(user: User)

}