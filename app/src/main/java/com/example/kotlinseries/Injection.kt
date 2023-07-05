package com.example.kotlinseries

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.example.kotlinseries.data.KotlinSeriesRepository
import com.example.kotlinseries.database.KotlinSeriesDatabase

object Injection {
    private fun provideKotlinSeriesRepository(context: Context): KotlinSeriesRepository {
        return KotlinSeriesRepository(KotlinSeriesDatabase.getDatabase(context))
    }

    fun provideViewModelFactory(
        context: Context,
        owner: SavedStateRegistryOwner
    ): ViewModelProvider.Factory {
        return ViewModelFactory(owner, provideKotlinSeriesRepository(context))
    }
}
