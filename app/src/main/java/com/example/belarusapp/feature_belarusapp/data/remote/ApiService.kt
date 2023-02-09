package com.example.belarusapp.feature_belarusapp.data.remote

import com.example.belarusapp.feature_belarusapp.data.model.CityDetailItemEntity
import com.example.belarusapp.feature_belarusapp.data.model.CityItemEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/get_cities/11")
    suspend fun getCityItem(
    ): List<CityItemEntity>

    @GET("api/get_points/12")
    suspend fun getCityDetailItem(
        @Query("id") id: Int,
    ):List<CityDetailItemEntity>

}
