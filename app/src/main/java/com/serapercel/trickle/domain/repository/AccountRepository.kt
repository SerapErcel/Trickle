package com.serapercel.trickle.domain.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.serapercel.trickle.data.entity.User
import com.serapercel.trickle.util.removePunctuation

class AccountRepository {
    var dbRef = FirebaseDatabase.getInstance().getReference("accounts")

    fun fetchAccounts(accountsLiveData: MutableLiveData<List<String>>, email: String) {
        val accountList = arrayListOf<String>()
        val userId = dbRef.push().key!!
        val user = User(userId, email)
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                accountList.clear()
                if (snapshot.exists()) {
                    for (empSnap in snapshot.child(user.email!!.removePunctuation()).children) {
                        val empData = empSnap.getKey().toString()
                        accountList.add(empData)
                    }
                    accountsLiveData.value = accountList
                }
            }

            // TODO Error message
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun addAccount(email: String, newAccount: String): Boolean {
        var result = true
        val userId = dbRef.push().key!!
        val user = User(userId, email)
        dbRef.child(user.email!!.removePunctuation()).child(newAccount).setValue(user)
            .addOnSuccessListener {
                result = true
            }.addOnFailureListener { exception ->
            exception.localizedMessage?.let {
                result = false
            }
        }
        return result
    }
}