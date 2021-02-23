package com.udacity.asteroidradar.domain

import androidx.room.ColumnInfo
import com.squareup.moshi.Json


data class PictureOfDay(
    @Json(name = "media_type")
    @ColumnInfo(name = "media_type")
    val mediaType:
    String, val title: String,
val url: String)

