package com.serapercel.trickle.data.source.remote

import com.serapercel.trickle.data.entity.Account
import com.serapercel.trickle.data.entity.ITransaction
import com.serapercel.trickle.data.entity.Need
import com.serapercel.trickle.data.entity.User

interface RemoteDataSource {

    /** Needs **/
    suspend fun getNeeds(user: User): List<Need>

    suspend fun addNeed(need: Need, user: User): Boolean

    suspend fun deleteNeed(need: Need, user: User): Boolean

    /** Transactions **/

    suspend fun getTransactions(account: Account): List<ITransaction>

    suspend fun getAllTransactions(user: User): List<ITransaction>

    suspend fun addTransaction(transaction: ITransaction, user: User): Boolean

    suspend fun deleteTransaction(transaction: ITransaction, user: User): Boolean

    suspend fun deleteAllTransactions(user: User)

}