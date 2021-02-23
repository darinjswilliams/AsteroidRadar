package com.udacity.asteroidradar.network

import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.database.AsteroidEntity
import com.udacity.asteroidradar.database.PictureOfDayEntity
import com.udacity.asteroidradar.domain.Asteroid


@JsonClass(generateAdapter = true)
data class NetworkPictureOfDayContainer(val pictureOfDay: NetworkPictureOfDay)

@JsonClass(generateAdapter = true)
data class NetworkPictureOfDay(
    val mediaType: String, val title: String, val url: String
)


fun NetworkPictureOfDay.asPictureOfDayDatabaseModel(): PictureOfDayEntity {
    return PictureOfDayEntity(
            mediaType = this.mediaType,
            title = this.title,
            url = this.url
        )
}


@JsonClass(generateAdapter = true)
data class NetworkAsteroidContainer(val asteroids: List<NetworkAsteroids>)

@JsonClass(generateAdapter = true)
data class NetworkAsteroids(
    val id: Long, val codename: String, val closeApproachDate: String,
    val absoluteMagnitude: Double, val estimatedDiameter: Double,
    val relativeVelocity: Double, val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

fun NetworkAsteroidContainer.asDomainModel(): List<Asteroid> {
    return asteroids.map {
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

fun List<NetworkAsteroids>.asDatabaseModel(): List<AsteroidEntity> {
    return map {
        AsteroidEntity(
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
