package com.example.belarusapp.feature_belarusapp.data.repository

import android.content.Context
import com.example.belarusapp.feature_belarusapp.data.model.CityDetailItemEntity
import com.example.belarusapp.feature_belarusapp.data.model.CityItemEntity
import com.example.belarusapp.feature_belarusapp.data.remote.ApiService
import com.example.belarusapp.feature_belarusapp.domain.repository.BoardGamesInfoRepository
import javax.inject.Inject

class BoardGamesInfoRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    val context: Context
) : BoardGamesInfoRepository {

    override suspend fun getCities(): List<CityItemEntity> =
        apiService.getCityItem()

    override suspend fun getPoints(id:Int): List<CityDetailItemEntity>
    = apiService.getCityDetailItem(id)
}
