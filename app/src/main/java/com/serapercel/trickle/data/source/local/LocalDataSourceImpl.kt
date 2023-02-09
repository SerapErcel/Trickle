package com.serapercel.trickle.data.source.local

import com.serapercel.trickle.data.entity.ITransaction
import com.serapercel.trickle.data.entity.Need
import com.serapercel.trickle.data.entity.User
import com.serapercel.trickle.data.source.database.NeedDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val needDao: NeedDao
) : LocalDataSource {

    /** Needs **/

    override fun readDatabase(): Flow<List<Need>> = needDao.getAllNeeds()

    override suspend fun insertNeeds(need: Need) =
        needDao.insert(need = need)


    override suspend fun insertAllNeeds(needList: List<Need>) =
        needDao.insertAll(needList = needList)


    override suspend fun deleteNeed(need: Need) =
        needDao.delete(need = need)


    override suspend fun deleteAllNeed() =
        needDao.deleteAllNeed()


    override suspend fun updateNeed(id: Int?, name: String?, count: String?) =
        needDao.update(id = id, name = name, count = count)


    /** Transactions **/

    override suspend fun getTransactions(user: User): Flow<List<ITransaction>> {
        TODO("Not yet implemented")
    }

    override suspend fun addTransaction(transaction: ITransaction, user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTransaction(transaction: ITransaction, user: User) {
        TODO("Not yet implemented")
    }

    override fun readTransactionsDb(): Flow<List<ITransaction>> {
        TODO("Not yet implemented")
    }

}