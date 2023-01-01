package com.serapercel.trickle.data.entity

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class User(val id: String? = null, val email: String? = null, var accountList: ArrayList<String>?) : Serializable
