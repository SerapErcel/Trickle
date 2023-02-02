package com.serapercel.trickle.data.source.remote

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.serapercel.trickle.data.entity.Need
import com.serapercel.trickle.data.entity.User
import com.serapercel.trickle.util.removePunctuation
import kotlinx.coroutines.delay
import javax.inject.Inject

class RemoteNeedDataSourceImpl @Inject constructor(
) : RemoteNeedDataSource {

    var dbRef = FirebaseDatabase.getInstance().getReference("accounts")
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
        delay(1000L)
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


}