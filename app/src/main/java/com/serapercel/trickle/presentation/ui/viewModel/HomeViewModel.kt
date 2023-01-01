package com.serapercel.trickle.presentation.ui.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel(){
    val accountLiveData = MutableLiveData<String>()

    fun getDataFromRoom(){
        val account = "SERAP QUEEN"
        accountLiveData.value = account
    }
}