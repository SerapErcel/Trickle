package com.serapercel.trickle.data.entity

import java.util.Date

interface ITransaction {
    val id: String
    val account: Account
    val title: String
    val price: Float
    val date: Date
    val income: Boolean
}