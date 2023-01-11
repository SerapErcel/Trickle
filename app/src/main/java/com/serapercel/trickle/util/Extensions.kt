package com.serapercel.trickle.util

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.serapercel.trickle.data.entity.Account
import com.serapercel.trickle.data.entity.User

// Sending a short toast message
fun Context.toastShort(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

// Sending a long toast message
fun Context.toastLong(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

// Remove punctuation marks from string
fun String.removePunctuation(): String {
    return this.replace("[\\p{Punct}]".toRegex(), "")
}

// Convert to Account
fun String.toAccount(): Account {
    val user= MutableLiveData<User>()
    user.value = User(this.split(" ",)[2],this.split(" ",)[1])
    return Account(this.split(" ",)[0], user)
}