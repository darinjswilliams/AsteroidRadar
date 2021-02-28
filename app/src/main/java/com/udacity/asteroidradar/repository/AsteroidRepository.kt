package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.*
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.domain.asPictureDatabaseModel
import com.udacity.asteroidradar.network.NetworkAsteroids
import com.udacity.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber

class AsteroidRepository(private val database: AsteroidDatabase) {

    private var asteroidFilter = MutableLiveData(AsteroidFilter.SHOW_TODAY)

    var currentDay = ParseDate.getToday()
    var endOfWeek = ParseDate.getOneWeekAhead()

    //convert from one livedata to another livedata, Filter data according to menu
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.switchMap(asteroidFilter) { menuFilter ->
            when (menuFilter) {
                AsteroidFilter.SHOW_TODAY -> Transformations.map(
                    database.asteroidDao
                        .getTodayAsteroids(currentDay)
                ) {
                    it.asAsteroidDomainModel()
                }

                AsteroidFilter.SHOW_WEEK -> Transformations.map(
                    database.asteroidDao
                        .getTodayAsteroids(endOfWeek)
                ) {
                    it.asAsteroidDomainModel()
                }

                else -> Transformations.map(
                    database.asteroidDao
                        .getWeeklyAsteroids(currentDay, endOfWeek)
                ) {
                    it.asAsteroidDomainModel()
                }
            }

        }

    val picOfDay: LiveData<PictureOfDay> = Transformations.map(
        database.pictureOfDayDao.getPictureOfToday()
    ) {
        it.asPictureDomainModel()
    }

    //Refersh the Offline Cache
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {

            try {

                val startDate = ParseDate.getToday()

                val endDate = ParseDate.getOneWeekAhead()

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
                val picImageOfDay = PictureApi.pictureService.getImageOfToday(Constants.key)

                picImageOfDay.let { database.pictureOfDayDao.insertPicture(it.asPictureDatabaseModel()) }

                Timber.i("Insert of picture of day into database")
            } catch (ex: Exception) {
                Timber.i("Error downloading PictureOfDay ${ex.localizedMessage}")
            }
        }
    }

    fun getAsteroidsByDate(filter: AsteroidFilter) {

        Timber.i("Filter Request was ---> ${filter.value} ")
        asteroidFilter.value = filter


    }

}


