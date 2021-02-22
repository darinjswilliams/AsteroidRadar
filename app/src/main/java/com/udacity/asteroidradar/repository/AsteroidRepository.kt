package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.PictureApi
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.network.NetworkAsteroids
import com.udacity.asteroidradar.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {

//    val asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao
//        .getTodayAsteroids(targetDate: String)){
//        it.asAsteroidDomainModel()
//    }

    //Refersh the Offline Cache
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val dateArrays = getNextSevenDaysFormattedDates()

            val startDate = dateArrays.get(0)

            val endDate = dateArrays.get(7)


            val picOfDay = PictureApi.pictureService.getImageOfToday(Constants.key)
            val asteroidResult =
                AsteroidApi.retrofitService.getAsteroids(startDate, endDate, Constants.key)

            val asteroidProperties = parseAsteroidsJsonResult(JSONObject(asteroidResult))
            val networkAsteroidList = asteroidProperties.map {
                NetworkAsteroids(
                    it.id,
                    it.codename,
                    it.closeApproachDate,
                    it.absoluteMagnitude,
                    it.estimatedDiameter,
                    it.relativeVelocity,
                    it.distanceFromEarth,
                    it.isPotentiallyHazardous
                )
            }
            database.asteroidDao.insertAsteroid(*networkAsteroidList.asDatabaseModel())

        }
    }


}