package com.example.belarusapp.feature_belarusapp.data.model

import com.example.belarusapp.feature_belarusapp.domain.model.CityDetail

data class CityDetailItemEntity(
    val city_id: Int,
    val creation_date: String,
    val id: Int,
    val id_point: Int,
    val images: List<String>,
    val is_excursion: Boolean,
    val lang: Int,
    val last_edit_time: Int,
    val lat: Double,
    val lng: Double,
    val logo: String,
    val name: String,
    val photo: String,
    val sound: String,
    val tags: List<Int>,
    val text: String,
    val visible: Boolean
)

fun CityDetailItemEntity.toCityDetail(): CityDetail {
    return CityDetail(
        city_id = city_id,
        creation_date = creation_date,
        id = id,
        id_point = id_point,
        lang = lang,
        logo = logo,
        name = name,
        photo = photo,
        sound = sound,
        text = text
    )
}
