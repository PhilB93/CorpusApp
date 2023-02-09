package com.example.belarusapp.feature_belarusapp.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.belarusapp.feature_belarusapp.domain.use_case.SearchBoardGamesUseCase
import com.example.belarusapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val searchBoardGamesUseCase: SearchBoardGamesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameListState())
    val state = _state.asStateFlow()

    private val _eventFlow = Channel<UIEvent>()
    val eventFlow = _eventFlow.receiveAsFlow()


  init {
      viewModelScope.launch {
            searchBoardGamesUseCase()
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




