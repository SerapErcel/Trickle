package com.serapercel.trickle.presentation.ui.viewModel

import android.app.Activity
import android.content.Intent
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.serapercel.trickle.data.entity.User
import com.serapercel.trickle.presentation.ui.activity.MainActivity
import com.serapercel.trickle.util.removePunctuation
import com.serapercel.trickle.util.toastLong

class AccountViewModel : ViewModel() {

    private lateinit var dbRef: DatabaseReference
    val accounts = MutableLiveData<List<String>>()
    val accountsError = MutableLiveData<Boolean>()
    // val accountsLoading = MutableLiveData<Boolean>()

    fun refreshAccountData(user: User) {
        val accountList = arrayListOf<String>()

        dbRef = FirebaseDatabase.getInstance().getReference("accounts")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                accountList.clear()
                if (snapshot.exists()) {
                    for (empSnap in snapshot.child(user.email!!.removePunctuation()).children) {
                        val empData = empSnap.getKey().toString()
                        accountList.add(empData)
                    }
                    accounts.value = accountList
                    accountsError.value = false
                    // accountsLoading.value = false
                }
            }
            // TODO Error message
            override fun onCancelled(error: DatabaseError) { }
        })

    }

    fun addAccount(user: User, newAccount: String, activity: Activity) {
        dbRef.child(user.email!!.removePunctuation()).child(newAccount).setValue(user).addOnSuccessListener {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }.addOnFailureListener { exception ->
            exception.localizedMessage?.let { it -> activity.toastLong(it) }
        }
    }

}