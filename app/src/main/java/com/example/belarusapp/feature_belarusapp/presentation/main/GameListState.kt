package com.example.belarusapp.feature_belarusapp.presentation.main

import com.example.belarusapp.feature_belarusapp.domain.model.CityItem

data class GameListState(
    val isLoading: Boolean = false,
    val games: List<CityItem> = emptyList(),
    val error: String = ""
)