package com.serapercel.trickle.data.entity

import android.os.Parcelable
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
data class Income(

    override val account: Account,
    override val title: String,
    override val price: String,
    override val date: String,

) : ITransaction, Parcelable {

    override val income: Boolean = true

    @PrimaryKey(autoGenerate = false)
    override var id: String = ""
}