package com.serapercel.trickle.presentation.ui.viewModel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.serapercel.trickle.data.entity.Need
import com.serapercel.trickle.domain.repository.NeedsRepository
import javax.inject.Inject
import com.serapercel.trickle.data.dataStore.DataStoreRepository
import com.serapercel.trickle.data.entity.User
import com.serapercel.trickle.util.NetworkResult
import com.serapercel.trickle.util.toastShort
import dagger.hilt.android.internal.Contexts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class NeedsViewModel @Inject constructor(
    private val repository: NeedsRepository,
    private val context: Context,
    private val dataStoreRepository: DataStoreRepository,
    private val user: User

) : ViewModel() {
    var networkStatus = false
    var backOnline = false

    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    private fun saveBackOnline(backOnline: Boolean) = viewModelScope.launch {
        dataStoreRepository.saveBackOnline(backOnline)
    }

    /** ROOM **/
    val reedNeeds: LiveData<List<Need>> = repository.readDatabase().asLiveData()

    /** Firebase **/
    private val _needsResponse: MutableLiveData<NetworkResult<List<Need>>> = MutableLiveData()
    val needsResponse: LiveData<NetworkResult<List<Need>>> = _needsResponse

    fun getNeeds(user: User) = viewModelScope.launch {
        getNeedsSafeCall(user)
    }

    private suspend fun getNeedsSafeCall(user: User) {
        _needsResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {

            try {
                val response = repository.getNeeds(user)
                _needsResponse.value = handleNeedsResponse(response = response)

                val ndsResponse = _needsResponse.value!!.data

                if (ndsResponse != null) {
                    offlineCacheRecipes(ndsResponse)
                }

            } catch (e: Exception) {
                _needsResponse.value = NetworkResult.Error(message = e.message)

            }
        } else {
            _needsResponse.value = NetworkResult.Error(message = "No Internet Connection.")
        }
    }

    private fun handleNeedsResponse(response: List<Need>): NetworkResult<List<Need>> {
        return when {
            response.isNotEmpty() -> {
                NetworkResult.Success(data = response)
            }
            else -> {
                NetworkResult.Error("Get Needs Firebase Error!")
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

    /** ROOM */
    private fun offlineCacheRecipes(needList: List<Need>) {
        insertNeed(needList = needList)

    }

    private fun insertNeed(needList: List<Need>) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllNeed()
            repository.insertAllNeeds(needList = needList)
        }

    private val _needDeleteResponse: MutableLiveData<NetworkResult<Boolean>> = MutableLiveData()
    // val needDeleteResponse: LiveData<NetworkResult<Boolean>> = _needDeleteResponse

    fun remoweNeed(need: Need, user: User) = viewModelScope.launch {
        deleteNeedSafeCall(need, user)
    }

    private fun deleteNeed(need: Need) = viewModelScope.launch {
        repository.deleteNeed(need, user)
    }

    private suspend fun deleteNeedSafeCall(need: Need, user: User) {
        _needDeleteResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {

            try {
                val response = repository.deleteNeed(need, user)
                _needDeleteResponse.value = handleNeedsResponse(response = response)

                val ndsResponse = _needDeleteResponse.value!!.data

                if (ndsResponse != null) {
                    deleteNeed(need)
                }

            } catch (e: Exception) {
                _needsResponse.value = NetworkResult.Error(message = e.message)

            }
        } else {
            _needsResponse.value = NetworkResult.Error(message = "No Internet Connection.")
        }
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