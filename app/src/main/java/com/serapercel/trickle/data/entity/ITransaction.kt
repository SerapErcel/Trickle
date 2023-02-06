package com.serapercel.trickle.data.entity

interface ITransaction {
    var id: String
    val account: Account
    val title: String
    val price: String
    val date: String
    val income: Boolean
}