package com.serapercel.trickle.presentation.ui.viewModel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serapercel.trickle.data.entity.Need
import com.serapercel.trickle.data.entity.User
import com.serapercel.trickle.domain.repository.NeedsRepository
import com.serapercel.trickle.util.NetworkResult
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNeedViewModel @Inject constructor(
    private val repository: NeedsRepository,
    private val context: Context,

    ) : ViewModel() {
    /** Firebase **/
    private val _needsResponse: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData()
    val needsResponse: LiveData<NetworkResult<Boolean>> = _needsResponse

    fun addNeed(need: Need, user: User) = viewModelScope.launch {
        addNeedSafeCall(need, user)
    }

    private suspend fun addNeedSafeCall(need: Need, user: User) {
        _needsResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {

            try {
                val response = repository.addNeed(need, user)
                _needsResponse.value = handleNeedsResponse(response = response)

                val ndsResponse = _needsResponse.value!!.data

                if (ndsResponse != null) {
                    offlineCacheRecipes(need)
                }

            } catch (e: Exception) {
                _needsResponse.value = NetworkResult.Error(message = e.message)

            }
        } else {
            _needsResponse.value = NetworkResult.Error(message = "No Internet Connection.")
        }
    }

    private fun offlineCacheRecipes(need: Need) {
        insertNeed(need = need)
    }

    private fun insertNeed(need: Need) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertNeeds(need = need)
        }

    private fun handleNeedsResponse(response: Boolean): NetworkResult<Boolean> {
        return when {
            response -> {
                NetworkResult.Success(data = response)
            }
            else -> {
                NetworkResult.Error("Add Needs Firebase Error!")
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = Contexts.getApplication(context).getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}