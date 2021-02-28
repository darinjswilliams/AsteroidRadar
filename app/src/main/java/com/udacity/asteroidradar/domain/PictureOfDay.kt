package com.udacity.asteroidradar.domain

import androidx.room.ColumnInfo
import com.squareup.moshi.Json
import com.udacity.asteroidradar.database.PictureOfDayEntity


data class PictureOfDay(
    @Json(name = "media_type")
    @ColumnInfo(name = "media_type")
    val mediaType:
    String, val title: String,
val url: String)



fun PictureOfDay.asPictureDatabaseModel() : PictureOfDayEntity {
    return PictureOfDayEntity(
        mediaType = this.mediaType,
        title = this.title,
        url = this.url
    )
}