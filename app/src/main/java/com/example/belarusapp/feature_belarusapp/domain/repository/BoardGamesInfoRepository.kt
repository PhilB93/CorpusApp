package com.example.belarusapp.feature_belarusapp.domain.repository

import com.example.belarusapp.feature_belarusapp.data.model.CityDetailItemEntity
import com.example.belarusapp.feature_belarusapp.data.model.CityItemEntity

interface BoardGamesInfoRepository {
    suspend fun getCities(
    ): List<CityItemEntity>

    suspend fun getPoints(id:Int): List<CityDetailItemEntity>
}