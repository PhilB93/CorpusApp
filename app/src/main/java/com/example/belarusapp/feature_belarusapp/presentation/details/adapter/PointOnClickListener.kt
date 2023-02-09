package com.example.belarusapp.feature_belarusapp.presentation.details.adapter

import com.example.belarusapp.feature_belarusapp.domain.model.CityDetail

interface PointOnClickListener {
    fun onClick(cityItem: CityDetail)
}