package com.serapercel.trickle.domain.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.serapercel.trickle.data.entity.User
import com.serapercel.trickle.util.removePunctuation

class AccountRepository {
    var dbRef = FirebaseDatabase.getInstance().getReference("accounts")
    private val userId = dbRef.push().key!!
    fun fetchAccounts(
        accountsLiveData: MutableLiveData<List<String>>,
        email: String,
        user: MutableLiveData<User>
    ) {
        val accountList = arrayListOf<String>()
        user.value = User(userId, email)

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                accountList.clear()
                if (snapshot.exists()) {
                    for (empSnap in snapshot.child(user.value!!.email!!.removePunctuation()).children) {
                        val empData = empSnap.key.toString()
                        if (empData != "needs" && empData != "transactions"){
                            accountList.add(empData)
                        }
                    }
                    accountsLiveData.value = accountList
                }
            }

            // TODO Error message
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun addAccount(email: String, newAccount: String, user: MutableLiveData<User>): Boolean {
        var result = true
        user.value = User(userId, email)
        dbRef.child(user.value!!.email!!.removePunctuation()).child(newAccount).setValue(user)
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