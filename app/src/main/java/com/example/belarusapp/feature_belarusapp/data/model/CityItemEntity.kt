package com.example.belarusapp.feature_belarusapp.data.model

import com.example.belarusapp.feature_belarusapp.domain.model.CityItem


data class CityItemEntity(
    val city_is_regional: Boolean,
    val id: Int,
    val id_locale: Int,
    val lang: Int,
    val last_edit_time: Int,
    val logo: String,
    val name: String,
    val region: String,
    val visible: Boolean
)

fun CityItemEntity.toCityItem(): CityItem {
    return CityItem(
        id = id,
        logo = logo,
        name = name,
    )
}