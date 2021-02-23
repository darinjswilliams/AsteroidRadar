package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay

@Entity(tableName = "picture_of_day_table")
data class PictureOfDayEntity constructor(

    @PrimaryKey
    val url: String,

    @ColumnInfo(name = "media_type")
    val mediaType: String,

    val title: String

)


@Entity(tableName = "asteroids_table")
data class AsteroidEntity constructor(
    @PrimaryKey
    val id: Long,
    val codename: String,

    @ColumnInfo(name = "close_approach_date")
    val closeApproachDate: String,

    @ColumnInfo(name = "absolute_magnitude")
    val absoluteMagnitude: Double,

    @ColumnInfo(name = "estimated_diameter")
    val estimatedDiameter: Double,

    @ColumnInfo(name = "relative_velocity")
    val relativeVelocity: Double,

    @ColumnInfo(name = "distance_from_earth")
    val distanceFromEarth: Double,

    @ColumnInfo(name = "ispotentially_hazardous")
    val isPotentiallyHazardous: Boolean

)

//Extension fuctions to convert from database object to domain object
fun List<AsteroidEntity>.asAsteroidDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}

fun  PictureOfDay.asPictureDatabaseModel() : PictureOfDayEntity {
    return PictureOfDayEntity(
            mediaType = this.mediaType,
            title = this.title,
            url = this.url
        )

}