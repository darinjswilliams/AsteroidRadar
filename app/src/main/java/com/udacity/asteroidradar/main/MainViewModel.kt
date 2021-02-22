package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.api.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber


enum class AsteroidApiStatus { LOADING, ERROR, DONE }
class MainViewModel : ViewModel() {

    // The internal MutableLiveData that stores the status of the most recent request
//External Variables
    private val _status = MutableLiveData<AsteroidApiStatus>()

    private val _asteroidProperties = MutableLiveData<List<Asteroid>>()
    private val _pictureOfToday = MutableLiveData<PictureOfDay>()
    private val _navigateToSelectedProperty = MutableLiveData<Asteroid>()

    val status: LiveData<AsteroidApiStatus>
        get() = _status

    val asteroidProperties: LiveData<List<Asteroid>>
        get() = _asteroidProperties

    val pictureOfToday: LiveData<PictureOfDay>
        get() = _pictureOfToday

    val navigateToSelectedProperty: LiveData<Asteroid>
        get() = _navigateToSelectedProperty


    init {
        getPictureOfToday()
        getAsteroidInformation(AsteroidFilter.SHOW_TODAY)
    }

    private fun getPictureOfToday() {
        viewModelScope.launch {
            try {
                _status.value = AsteroidApiStatus.LOADING
                Timber.i("Loading Picture of Day ${_status.value}")

                _pictureOfToday.value = PictureApi.pictureService.getImageOfToday(Constants.key)



                _status.value = AsteroidApiStatus.DONE
                Timber.i("Loading Picture of Day ${_status.value}")

            } catch (e: Exception) {
                _status.value = AsteroidApiStatus.ERROR
                Timber.i("Error Loading Picture of Day ${_status.value}")

            }
        }
    }

    private fun getAsteroidInformation(filter: AsteroidFilter) {
        viewModelScope.launch {
            try {
                _status.value = AsteroidApiStatus.LOADING


                Timber.i("Display data ${filter}")
                val dateArrays = getNextSevenDaysFormattedDates()

                val startDate = dateArrays.get(0)

                val endDate = dateArrays.get(7)


                val jsonResult = AsteroidApi.retrofitService.getAsteroids(
                    startDate,
                    endDate,
                    Constants.key
                )

                _asteroidProperties.value = parseAsteroidsJsonResult(JSONObject(jsonResult))

                _status.value = AsteroidApiStatus.DONE

                Timber.i("${_status.value}")
            } catch (e: Exception) {
                _status.value = AsteroidApiStatus.ERROR

                Timber.i("Failed retrieving asteroid data  ")
                _asteroidProperties.value = ArrayList()

                Timber.i("${_status.value}")
            }


        }

    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedProperty.value = asteroid
    }

    //To prevent unwanted navigation extra navigation
    fun displayAsteroidDetailsCompleted() {
        _navigateToSelectedProperty.value = null
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

