package com.serapercel.trickle.presentation.ui.viewModel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
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
    private val dataStoreRepository: DataStoreRepository

) : ViewModel() {
    var networkStatus = false
    var backOnline = false

    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    private fun saveBackOnline(backOnline: Boolean) = viewModelScope.launch {
        dataStoreRepository.saveBackOnline(backOnline)
        Log.e("hata", "ViewModel saveBackOnline - backOnline ${backOnline}")

    }

    /** ROOM **/
    val reedNeeds: LiveData<List<Need>> = repository.readDatabase().asLiveData()

    /** Firebase **/
    private val _needsResponse: MutableLiveData<NetworkResult<List<Need>>> = MutableLiveData()
    val needsResponse: LiveData<NetworkResult<List<Need>>> = _needsResponse

    fun getNeeds(user: User) = viewModelScope.launch {
        getNeedsSafeCall(user)
        Log.e("hata", "getNeeds firebase tetiklendi ")

    }

    private suspend fun getNeedsSafeCall(user: User) {
        Log.e("hata","get needs safe call")
        _needsResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            Log.e("hata","get needs safe call hasInternet connection tetiklendi")

            try {
                val response = repository.getNeeds(user)
                _needsResponse.value = handleNeedsResponse(response = response)

                val ndsResponse = _needsResponse.value!!.data

                if (ndsResponse !=null) {
                    offlineCacheRecipes(ndsResponse)
                    Log.e("hata","getNeedsSafeCall if ${ndsResponse.size}")

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
        Log.e("hata", "offlineCacheRecipes ${needList.size}")
            insertNeed(needList = needList)

    }

    private fun insertNeed(needList: List<Need>) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllNeed()
            repository.insertAllNeeds(needList = needList)
        }

    fun deleteNeed(need: Need)= viewModelScope.launch {
        repository.deleteNeed(need)
    }

    fun addNeed(need: Need)= viewModelScope.launch {
        repository.insertNeeds(need)
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Log.e("hata", "showNetworkStatus  if - network status ${networkStatus}")
            context.toastShort("No Internet Connetciton")
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Log.e("hata", "showNetworkStatus else - network status ${networkStatus}")
                context.toastShort("We're back online.")
                saveBackOnline(false)
            }
        }
    }



}