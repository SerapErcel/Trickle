package com.serapercel.trickle.presentation.ui.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serapercel.trickle.data.dataStore.DataStoreRepository
import com.serapercel.trickle.data.entity.Account
import com.serapercel.trickle.data.entity.ITransaction
import com.serapercel.trickle.domain.repository.TransactionRepository
import com.serapercel.trickle.util.NetworkResult
import com.serapercel.trickle.util.hasInternetConnection
import com.serapercel.trickle.util.toastShort
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val repository: TransactionRepository,
    private val context: Context,
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {

    var networkStatus = false
    var backOnline = false

    private fun saveBackOnline(backOnline: Boolean) = viewModelScope.launch {
        dataStoreRepository.saveBackOnline(backOnline)
    }

    /** Firebase **/
    private val _transactionResponse: MutableLiveData<NetworkResult<List<ITransaction>>> =
        MutableLiveData()
    val transactionResponse: LiveData<NetworkResult<List<ITransaction>>> = _transactionResponse

    fun getAllTransactions(account: Account) = viewModelScope.launch {
        getAllTransactionsSafeCall(account)
    }

    private suspend fun getAllTransactionsSafeCall(account: Account) {
        _transactionResponse.value = NetworkResult.Loading()
        if (hasInternetConnection(context)) {
            try {
                val response = repository.getAllTransactions(account.user)
                _transactionResponse.value = handleResponse(response = response)

            } catch (e: Exception) {
                _transactionResponse.value = NetworkResult.Error(message = e.message)

            }
        } else {
            _transactionResponse.value = NetworkResult.Error(message = "No Internet Connection.")
        }
    }

    private fun handleResponse(response: List<ITransaction>): NetworkResult<List<ITransaction>> {
        return when {
            response.isNotEmpty() -> {
                NetworkResult.Success(data = response)
            }
            else -> {
                NetworkResult.Error("Get Transactions Firebase Error!")
            }
        }
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            context.toastShort("No Internet Connection")
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                context.toastShort("We're back online.")
                saveBackOnline(false)
            }
        }
    }
}