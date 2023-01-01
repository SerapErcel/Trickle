package com.serapercel.trickle.presentation.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AccountViewModel : ViewModel(){

    val accounts = MutableLiveData<List<String>>()
    val accountsError = MutableLiveData<Boolean>()
    // val accountsLoading = MutableLiveData<Boolean>()

    fun refreshData(){
        val account1 = "Serap"
        val account2 = "Serhat "
        val account3 = "Aysel"
        val account4 = "Lutfu"

        val accountList= arrayListOf<String>(account1,account2,account3,account4,account2,account3,account4)

        accounts.value = accountList
        accountsError.value = false
        // accountsLoading.value = false

    }

}