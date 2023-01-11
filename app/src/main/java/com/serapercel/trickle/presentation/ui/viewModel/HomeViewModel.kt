package com.serapercel.trickle.presentation.ui.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.serapercel.trickle.data.entity.Account
import com.serapercel.trickle.util.toAccount


class HomeViewModel : ViewModel(){
    val accountLiveData = MutableLiveData<Account>()

    fun getDataFromSharedPref(context: Context){
        val account = getSharedPref(context).name
        //accountLiveData.value = account!!
    }
    fun getSharedPref(context: Context): Account {
        val sharedPreference =  context.getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE)
        return sharedPreference.getString("account","defVAlue is comming")!!.toAccount()
    }
}