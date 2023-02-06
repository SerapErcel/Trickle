package com.serapercel.trickle.presentation.ui.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serapercel.trickle.data.entity.ITransaction
import com.serapercel.trickle.data.entity.User
import com.serapercel.trickle.domain.repository.TransactionRepository
import com.serapercel.trickle.util.NetworkResult
import com.serapercel.trickle.util.handleNeedsResponse
import com.serapercel.trickle.util.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val repository: TransactionRepository,
    private val context: Context,
) : ViewModel() {
    fun addTransaction(transaction: ITransaction, user: User) = viewModelScope.launch {
        addTransactionSafeCall(transaction, user)
    }

    private val _transactionResponse: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData()
    val transactionResponse: LiveData<NetworkResult<Boolean>> = _transactionResponse

    private suspend fun addTransactionSafeCall(transaction: ITransaction, user: User) {
        _transactionResponse.value = NetworkResult.Loading()
        if (hasInternetConnection(context)) {
            try {
                val response = repository.addTransaction(transaction, user)
                _transactionResponse.value = handleNeedsResponse(response = response)

            } catch (e: Exception) {
                _transactionResponse.value = NetworkResult.Error(message = e.message)
            }
        } else {
            _transactionResponse.value = NetworkResult.Error(message = "No Internet Connection!")
        }


    }
}