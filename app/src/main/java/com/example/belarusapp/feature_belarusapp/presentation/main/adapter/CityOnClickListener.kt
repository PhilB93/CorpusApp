package com.example.belarusapp.feature_belarusapp.presentation.main.adapter

import com.example.belarusapp.feature_belarusapp.domain.model.CityItem

interface CityOnClickListener {
    fun onClick(cityItem: CityItem)
}