package com.serapercel.trickle.data.entity

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
open class ITransaction(
    open val account: Account = Account("", User("", "")),
    open val title: String = "",
    open val price: String = "",
    open val date: String = "",

    ) : Parcelable {

    open val income: Boolean = false

    @PrimaryKey(autoGenerate = false)
    open var id: String = ""
}