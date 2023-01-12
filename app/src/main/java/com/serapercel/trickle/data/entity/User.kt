package com.serapercel.trickle.data.entity

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class User(val id: String? = null, val email: String? = null) : Parcelable
