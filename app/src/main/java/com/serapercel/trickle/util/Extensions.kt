package com.serapercel.trickle.util

import android.content.Context
import android.util.Log
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
    user.value = User("123","email@gmail.com")
    val name = this.split(",",)[0].substring(13)
    Log.e("toAccount this",this)
    // ToDo User convert string operation fix
    // ToDo string convert User operation fix
    // E/toAccount this: Account(name=aysel, user=androidx.lifecycle.MutableLiveData@2bd8ff4)
    Log.e("toAccount str",name)
    return Account("name", user)
}