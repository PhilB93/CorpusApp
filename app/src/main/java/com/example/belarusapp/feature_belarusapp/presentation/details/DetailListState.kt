package com.example.belarusapp.feature_belarusapp.presentation.details

import com.example.belarusapp.feature_belarusapp.domain.model.CityDetail

data class DetailListState(
    val isLoading: Boolean = false,
    val games: List<CityDetail> = emptyList(),
    val error: String = ""
)