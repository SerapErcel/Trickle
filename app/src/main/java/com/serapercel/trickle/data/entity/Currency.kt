package com.serapercel.trickle.data.entity

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@Parcelize
@IgnoreExtraProperties
data class Currency(
    val Isim: String,
    val CurrencyName: String,
    val ForexBuying: String,
    val ForexSelling: String,
    val BanknoteBuying: String,
    val BanknoteSelling: String
) :
    Parcelable
