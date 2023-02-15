package com.serapercel.trickle.presentation.ui.viewModel

import android.content.Context
import android.util.Log
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
    lateinit var _accountData : List<String>
    var priceData = ArrayList<Float>()
    var accounts = ArrayList<String>()

    fun getAllTransactions(account: Account) = viewModelScope.launch {
        Log.e("hata", "get all transactions")
        getData(account)
    }

    private suspend fun getData(account: Account) {
        try {
            Log.e("hata", "get data try")

            transactionsViewModel.getAllTransactions(account)
            delay(2000)

            accountsViewModel.fetchAccounts(account.user.email!!)
            delay(2000)

            val transactionDataTemp = transactionsViewModel.transactionResponse.value!!.data

            Log.e("hata", "get data try transaction complete ${transactionDataTemp!!.size}")

            var accountDataTemp = accountsViewModel.accounts.value

            Log.e("hata", "get data try account complete")

            if (!transactionDataTemp.isNullOrEmpty() && !accountDataTemp.isNullOrEmpty()) {
                Log.e("hata", "get data if null check")
                accountDataTemp = accountDataTemp.filter { it != "needs" && it != "transactions" }
                _accountData= accountDataTemp
                accounts.addAll(_accountData)
                transactionData = transactionDataTemp
                calucate()
            }

        } catch (e: Exception) {
            Log.e("hata", "get data catch")
            context.toastShort("Error while pulling data!")
        }
    }

    private fun calucate() {
        for (a in accounts) {

            Log.e("hata", "getAllTransactions account check")

            var total = 0f
            val list = transactionData.filter { it.account.name == a }
            for (i in list) {
                total += i.price.toFloat()
                Log.e("hata", "account: $a total: $total")

            }
            priceData.add(total)
        }
        Log.e("hata", "transaction data: ${transactionData.size} prices: $priceData accounts: $accounts")
    }
}