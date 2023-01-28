package com.serapercel.trickle.presentation.ui.viewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.serapercel.trickle.data.entity.Need
import com.serapercel.trickle.domain.repository.NeedsRepository
import javax.inject.Inject
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class NeedsViewModel @Inject constructor(
    private val repository: NeedsRepository,
    private val context: Context
): ViewModel(){
    val reedNeeds: LiveData<List<Need>> = repository.readDatabase().asLiveData()
}