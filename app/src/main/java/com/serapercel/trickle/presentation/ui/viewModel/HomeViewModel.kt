package com.serapercel.trickle.presentation.ui.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serapercel.trickle.data.entity.Account
import com.serapercel.trickle.util.toAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel() {
    val accountLiveData = MutableLiveData<Account>()

    fun getAccountData(context: Context){
        val account = getSharedPref(context)
        accountLiveData.value = account
    }
    private fun getSharedPref(context: Context): Account {
        val sharedPreference =  context.getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE)
        return sharedPreference.getString("account","defVAlue is comming")!!.toAccount()
    }

}