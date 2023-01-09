package com.serapercel.trickle.presentation.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serapercel.trickle.domain.repository.AccountRepository

class AccountViewModel : ViewModel() {

    private val repository = AccountRepository()

    val accounts = MutableLiveData<List<String>>()

    fun fetchAccounts(email: String){
        repository.fetchAccounts(accounts, email)
    }

    fun addAccount(email: String, newAccount: String): Boolean {
        return repository.addAccount(email,newAccount)
    }


}