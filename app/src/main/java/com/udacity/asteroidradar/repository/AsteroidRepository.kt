package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.PictureApi
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asAsteroidDomainModel
import com.udacity.asteroidradar.database.asPictureDatabaseModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.network.NetworkAsteroids
import com.udacity.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class AsteroidRepository(private val database: AsteroidDatabase, targetDate: String) {

    //convert from one livedata to another livedata
    val asteroids: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDao
            .getTodayAsteroids(targetDate)
    ) {
        it.asAsteroidDomainModel()
    }

    //Return Picture of Day
    fun getPictureOfDay() = database.pictureOfDayDao.getPictureOfToday()

    //Refersh the Offline Cache
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {

            try {
                val dateArrays = getNextSevenDaysFormattedDates()

                val startDate = dateArrays.get(0)

                val endDate = dateArrays.get(7)

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

                database.asteroidDao.insertAsteroid(
                    *networkAsteroidList.asDatabaseModel().toTypedArray()
                )
                Timber.i("Success insertion of Asteroid Data")

            } catch (ex: Exception) {
                Timber.i("Error on inserting asteroid data ${ex.localizedMessage}")
            }

        }
    }


    suspend fun refreshPictureOfDay() {
        withContext(Dispatchers.IO) {

            try {
                val picOfDay = PictureApi.pictureService.getImageOfToday(Constants.key)

                picOfDay?.let { database.pictureOfDayDao.insertPicture(it.asPictureDatabaseModel()) }

                Timber.i("Insert of picture of day into database")
            } catch (ex: Exception) {
                Timber.i("Error downloading PictureOfDay ${ex.localizedMessage}")
            }
        }
    }
}


