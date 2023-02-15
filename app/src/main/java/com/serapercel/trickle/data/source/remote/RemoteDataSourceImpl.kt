package com.serapercel.trickle.data.source.remote

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.serapercel.trickle.data.entity.*
import com.serapercel.trickle.util.removePunctuation
import kotlinx.coroutines.delay
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
) : RemoteDataSource {

    var dbRef = FirebaseDatabase.getInstance().getReference("accounts")

    /** Needs **/

    val needList = arrayListOf<Need>()

    override suspend fun getNeeds(user: User): List<Need> {

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                needList.clear()
                if (snapshot.exists()) {
                    for (empSnap in snapshot.child(user.email!!.removePunctuation())
                        .child("needs").children) {
                        needList.add(empSnap.getValue(Need::class.java)!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        delay(2000L)
        return needList
    }

    override suspend fun addNeed(need: Need, user: User): Boolean {
        var result = true

        val needId = dbRef.push().key!!
        need.id = needId

        dbRef.child(user.email!!.removePunctuation()).child("needs").child(needId).setValue(need)
            .addOnSuccessListener {
                result = true
            }.addOnFailureListener { exception ->
                exception.localizedMessage?.let {
                    result = false
                }
            }
        return result
    }

    override suspend fun deleteNeed(need: Need, user: User): Boolean {
        var result = true
        dbRef.child(user.email!!.removePunctuation()).child("needs").child(need.id).removeValue()
            .addOnSuccessListener {
                result = true
            }.addOnFailureListener { exception ->
                exception.localizedMessage?.let {
                    result = false
                }
            }
        return result
    }

    /** Transactions **/

    val transactionList = arrayListOf<ITransaction>()

    override suspend fun getTransactions(account: Account): List<ITransaction> {

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                transactionList.clear()
                if (snapshot.exists()) {
                    for (empSnap in snapshot.child(account.user.email!!.removePunctuation())
                        .child("transactions").children) {
                        if (empSnap.child("account").child("name").value == account.name) {
                            transactionList.add(empSnap.getValue(ITransaction::class.java)!!)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        delay(2000L)
        return transactionList
    }

    override suspend fun getAllTransactions(user: User): List<ITransaction> {


        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                transactionList.clear()
                if (snapshot.exists()) {
                    for (empSnap in snapshot.child(user.email!!.removePunctuation())
                        .child("transactions").children) {
                        transactionList.add(empSnap.getValue(ITransaction::class.java)!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        delay(2000L)
        return transactionList
    }

    override suspend fun addTransaction(transaction: ITransaction, user: User): Boolean {
        var result = true

        val transactionId = dbRef.push().key!!
        transaction.id = transactionId

        dbRef.child(user.email!!.removePunctuation()).child("transactions").child(transactionId)
            .setValue(transaction)
            .addOnSuccessListener {
                result = true
            }.addOnFailureListener { exception ->
                exception.localizedMessage?.let {
                    result = false
                }
            }
        return result
    }

    override suspend fun deleteTransaction(transaction: ITransaction, user: User): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTransactions(user: User) {
        TODO("Not yet implemented")
    }

}