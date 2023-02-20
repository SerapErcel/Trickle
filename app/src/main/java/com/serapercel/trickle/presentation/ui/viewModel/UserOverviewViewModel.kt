package com.serapercel.trickle.presentation.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serapercel.trickle.data.entity.Account
import com.serapercel.trickle.util.toastShort
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserOverviewViewModel @Inject constructor(
    private val context: Context
) : ViewModel() {

    lateinit var transactionsViewModel: LastTransactionsViewModel
    var priceData = ArrayList<Float>()

    fun getTransactions(account: Account) = viewModelScope.launch {
        getData(account)
    }

    private suspend fun getData(account: Account) {

        try {
            transactionsViewModel.getTransactions(account)
            delay(2000)

            val transactionDataTemp = transactionsViewModel.transactionResponse.value!!.data

            if (!transactionDataTemp.isNullOrEmpty()){

                var totalIncome = 0f
                var totalExpense = 0f
                for ( i in transactionDataTemp){
                    if (i.income){
                        totalIncome+=i.price.toFloat()
                    }else{
                        totalExpense+=i.price.toFloat()
                    }
                }
                priceData.add(totalIncome)
                priceData.add(totalExpense)
            }
        } catch (e: Exception) {
            context.toastShort("Error while pulling data!")
        }
    }

}