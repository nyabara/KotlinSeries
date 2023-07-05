package com.example.kotlinseries

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.kotlinseries.data.KotlinSeriesRepository
import com.example.kotlinseries.viewmodels.KotlinSeriersViewModel

class ViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val repository: KotlinSeriesRepository
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(KotlinSeriersViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KotlinSeriersViewModel(repository, handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
