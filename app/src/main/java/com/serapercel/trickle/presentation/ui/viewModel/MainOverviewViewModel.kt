package com.serapercel.trickle.presentation.ui.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serapercel.trickle.data.entity.Account
import com.serapercel.trickle.data.entity.ITransaction
import com.serapercel.trickle.util.toastShort
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainOverviewViewModel @Inject constructor(
    private val context: Context
) : ViewModel() {

    lateinit var transactionsViewModel: TransactionsViewModel
    lateinit var accountsViewModel: AccountViewModel

    lateinit var transactionData: List<ITransaction>
    private lateinit var _accountData: List<String>
    var priceData = ArrayList<Float>()
    var accounts = ArrayList<String>()

    fun getAllTransactions(account: Account) = viewModelScope.launch {
        getData(account)
    }

    private suspend fun getData(account: Account) {
        try {
            transactionsViewModel.getAllTransactions(account)
            delay(2000)

            accountsViewModel.fetchAccounts(account.user.email!!)
            delay(2000)

            val transactionDataTemp = transactionsViewModel.transactionResponse.value!!.data

            var accountDataTemp = accountsViewModel.accounts.value

            if (!transactionDataTemp.isNullOrEmpty() && !accountDataTemp.isNullOrEmpty()) {
                accountDataTemp = accountDataTemp.filter { it != "needs" && it != "transactions" }
                _accountData = accountDataTemp
                accounts.addAll(_accountData)
                transactionData = transactionDataTemp
                calucate()
            }

        } catch (e: Exception) {
            context.toastShort("Error while pulling data!")
        }
    }

    private fun calucate() {
        var total = 0f
        for (a in accounts) {

            for (t in transactionData) {
                if (t.account.name == a) {
                    if(t.income){
                        total += t.price.toFloat()
                    }else
                        total -= t.price.toFloat()
                }
            }
            priceData.add(total)
            total = 0f
        }
    }
}