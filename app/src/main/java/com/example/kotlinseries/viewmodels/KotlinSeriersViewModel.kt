package com.example.kotlinseries.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.kotlinseries.data.KotlinSeriesRepository
import com.example.kotlinseries.database.HouseEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class KotlinSeriersViewModel(
    private val kotlinSeriesRepository: KotlinSeriesRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val state: StateFlow<UiState>
    val pagingDataFlow: Flow<PagingData<UiModel>>
    val accept: (UiAction) -> Unit

    init {
        val initialQuery: String = savedStateHandle.get(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        val lastQueryScrolled: String = savedStateHandle.get(LAST_QUERY_SCROLLED) ?: DEFAULT_QUERY

        val actionStateFlow = MutableSharedFlow<UiAction>()
        val searches = actionStateFlow
            .filterIsInstance<UiAction.Search>() //Returns a flow containing only values that are instances of the given klass
            .distinctUntilChanged()
            .onStart { emit(UiAction.Search(query = initialQuery)) }
        val queriesScrolled = actionStateFlow
            .filterIsInstance<UiAction.Scroll>()
            .distinctUntilChanged()
            // This is shared to keep the flow "hot" while caching the last query scrolled,
            // otherwise each flatMapLatest invocation would lose the last query scrolled,
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
            .onStart { emit(UiAction.Scroll(currentQuery = lastQueryScrolled)) }

        pagingDataFlow = searches
            .flatMapLatest { searchHouse(queryString = it.query) } //Returns a flow that
            // switches to a new flow produced by transform function every time the original flow emits a value
            .cachedIn(viewModelScope)


        state = combine(
            searches,
            queriesScrolled,
            ::Pair
        ).map { (search, scroll) -> //map-> Returns a flow containing
            //the results of applying the given transform function to each value of the original flow
            UiState(
                query = search.query,
                lastQueryScrolled = scroll.currentQuery,
                // If the search query matches the scroll query, the user has scrolled
                hasNotScrolledForCurrentSearch = search.query != scroll.currentQuery
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = UiState()
            )

        accept = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }


    }

    private fun searchHouse(queryString: String): Flow<PagingData<UiModel>> =
        kotlinSeriesRepository.getSearchResultStream(queryString)
            .map { pagingData -> pagingData.map { UiModel.HouseItem(it) } }

}


sealed class UiAction {
    data class Search(val query: String) : UiAction()
    data class Scroll(val currentQuery: String) : UiAction()
}

data class UiState(
    val query: String = DEFAULT_QUERY,
    val lastQueryScrolled: String = DEFAULT_QUERY,
    val hasNotScrolledForCurrentSearch: Boolean = false
)

sealed class UiModel {
    data class HouseItem(val house: HouseEntry) : UiModel()
}

private const val LAST_QUERY_SCROLLED: String = "last_query_scrolled"


private const val LAST_SEARCH_QUERY: String = "last_search_query"

private const val DEFAULT_QUERY =
    "1km,Housetype=Single Room,Water=true,Watchman=true,Compound=true,Electricity=true"
