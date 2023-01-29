package com.serapercel.trickle.presentation.ui.viewModel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.serapercel.trickle.data.entity.Need
import com.serapercel.trickle.domain.repository.NeedsRepository
import javax.inject.Inject
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.serapercel.trickle.data.dataStore.DataStoreRepository
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


    // if we enable internet access again
    fun saveBackOnline(backOnline: Boolean) = viewModelScope.launch {
        dataStoreRepository.saveBackOnline(backOnline)
    }

    /** ROOM **/
    val reedNeeds: LiveData<List<Need>> = repository.readDatabase().asLiveData()


    fun showNetworkStatus() {
        if (!networkStatus) {
            context.toastShort("No Internet Connetciton")
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                context.toastShort("We're back online.")
                saveBackOnline(false)
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
    private fun offlineCacheRecipes(need: Need) {
        val newNeed = Need(need.name, need.count)
        insertNeed(need = newNeed)
    }

    private fun insertNeed(need: Need) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertNeeds(need = need)
        }

}