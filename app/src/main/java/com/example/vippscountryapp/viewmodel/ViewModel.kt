package com.example.vippscountryapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.vippscountryapp.network.Api
import com.example.vippscountryapp.network.CountryName
import java.io.IOException

sealed interface UiState {
    data class Success (val countryName: List<CountryName>) : UiState
    object Error : UiState
    object Loading : UiState
}

class AppViewModel : ViewModel() {
    var uiState by mutableStateOf<UiState>(UiState.Loading)
    var searchResults by mutableStateOf<List<CountryName>>(emptyList())

    fun searchCountry(query: String) {
        viewModelScope.launch {
            uiState = try {
                val result = Api.retrofitService.getCountryByName(query)
                searchResults = result
                UiState.Success(result)
            } catch (e: IOException) {
                UiState.Error
            }
        }
    }
}
