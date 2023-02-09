package com.example.belarusapp.feature_belarusapp.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CityItem(
    val id: Int,
    val logo: String,
    val name: String
): Parcelable
