package com.example.belarusapp.feature_belarusapp.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.belarusapp.feature_belarusapp.domain.use_case.GetPointsUseCase
import com.example.belarusapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getPointsUseCase: GetPointsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DetailListState())
    val state = _state.asStateFlow()

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()

    //private var _searchQuery = MutableStateFlow(5)
    private var searchJob: Job? = null


    fun onSearch(query: Int) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            getPointsUseCase(query)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _state.value = state.value.copy(
                                games = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                        is Resource.Error -> {
                            _state.value = state.value.copy(
                                games = result.data ?: emptyList(),
                                isLoading = false
                            )
                            _eventFlow.send(
                                UIEvent.ShowSnackbar(
                                    result.message ?: "Unknown error"
                                )
                            )
                        }
                        is Resource.Loading -> {
                            _state.value = state.value.copy(
                                isLoading = true,
                                games = result.data ?: emptyList()
                            )
                        }
                    }
                }
        }
    }


    sealed class UIEvent {
        data class ShowSnackbar(val message: String) : UIEvent()
    }
}




