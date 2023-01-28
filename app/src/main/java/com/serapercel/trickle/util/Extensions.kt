package com.serapercel.trickle.util

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
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
    val user = User(this.split(" ")[2], this.split(" ")[1])
    return Account(this.split(" ")[0], user)
}

// Fragment View
fun replaceFragment(requireActivity: FragmentActivity, container: Int, fragment: Fragment) {
    val manager = requireActivity.supportFragmentManager
    val fragmentTransaction = manager.beginTransaction()
    fragmentTransaction.replace(container, fragment)
    fragmentTransaction.commit()
}

fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
    observe(lifecycleOwner, object : Observer<T> {
        override fun onChanged(t: T) {
            removeObserver(this)
            observer.onChanged(t)
        }
    })
}