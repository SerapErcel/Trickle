package com.serapercel.trickle.data.entity

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Expense(

    override val account: Account,
    override val title: String,
    override val price: String,
    override val date: String,

    ) : ITransaction(), Parcelable {

    @IgnoredOnParcel
    override val income: Boolean = false

    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = false)
    override var id: String = ""
}
