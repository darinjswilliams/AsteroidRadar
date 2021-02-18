package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.database.ImageOfToday
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber

enum class AsteroidApiStatus { LOADING, ERROR, DONE }
class MainViewModel : ViewModel() {

// The internal MutableLiveData that stores the status of the most recent request
//External Variables
    private val _status = MutableLiveData<AsteroidApiStatus>()
    private val _asteroidProperties = MutableLiveData<List<Asteroid>>()
    private val _imageOfToday = MutableLiveData<ImageOfToday>()

    val status: LiveData<AsteroidApiStatus>
        get() = _status

    val asteroidProperties: LiveData<List<Asteroid>>
        get() = _asteroidProperties

    val imageOfToday: LiveData<ImageOfToday>
        get() = _imageOfToday

    init {
        getAsteroidInformation(AsteroidFilter.SHOW_TODAY)
    }

    private fun getAsteroidInformation(filter: AsteroidFilter) {
        viewModelScope.launch {
            _status.value = AsteroidApiStatus.LOADING

            try {

                val dateArrays = getNextSevenDaysFormattedDates()

                val startDate = dateArrays.get(0)

                val endDate = dateArrays.get(3)

                val jsonResult =
                    AsteroidApi.retrofitService.getAsteroids(
                        startDate,
                        endDate,
                        Constants.key
                    )

                val asteroids = parseAsteroidsJsonResult(JSONObject(jsonResult))

                _asteroidProperties.value = asteroids

                _status.value = AsteroidApiStatus.DONE

                Timber.i("${_status.value}")

            } catch (e: Exception) {

                _status.value = AsteroidApiStatus.ERROR

                _asteroidProperties.value = ArrayList()

                Timber.i("${_status.value}")
            }
        }
    }

    /**
     * Updates the data set filter for the web services by querying the data with the new filter
     * by calling [getAsteroidInformation]
     * @param filter the [AsteroidApiFilter] that is sent as part of the web server request
     */
    fun updateFilter(filter: AsteroidFilter) {
        getAsteroidInformation(filter)
    }
}

