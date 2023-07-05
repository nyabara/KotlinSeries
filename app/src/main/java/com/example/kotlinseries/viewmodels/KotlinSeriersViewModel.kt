package com.example.kotlinseries.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.kotlinseries.data.KotlinSeriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.time.Month

class KotlinSeriersViewModel(
    private val kotlinSeriesRepository: KotlinSeriesRepository,
    private val savedStateHandle: SavedStateHandle):ViewModel() {

    private val _uiState = MutableStateFlow(DateStateFlow())
    val uiState:StateFlow<DateStateFlow> = _uiState
    fun getInputToCalculateAge(day:Int,month: Int,year: Int){

        _uiState.update { currentStateFlow ->
            
            val dateReturned = day.toString()+ "/"+month.toString()+"/"+year.toString()
            currentStateFlow.copy(dob = dateReturned )
        }

    }






}

data class DateStateFlow(val dob:String? = null)