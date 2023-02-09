package com.example.belarusapp.feature_belarusapp.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CityDetail(
    val city_id: Int,
    val creation_date: String,
    val id: Int,
    val id_point: Int,
    val lang: Int,
    val logo: String,
    val name: String,
    val photo: String,
    val sound: String,
    val text: String
):Parcelable
