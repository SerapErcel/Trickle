package com.serapercel.trickle.data.entity

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Account(
    val name: String?,
    val user: MutableLiveData<User>?
) :
    Serializable
