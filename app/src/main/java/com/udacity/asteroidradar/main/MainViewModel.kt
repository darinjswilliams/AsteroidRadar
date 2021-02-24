package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.AsteroidFilter
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import timber.log.Timber


enum class AsteroidApiStatus { LOADING, ERROR, DONE }
class MainViewModel(application: Application) : AndroidViewModel(application) {

    // The internal MutableLiveData that stores the status of the most recent request
//External Variables
    private val _status = MutableLiveData<AsteroidApiStatus>()

    private val _pictureOfToday = MutableLiveData<PictureOfDay>()
    private val _navigateToSelectedProperty = MutableLiveData<Asteroid>()

    val status: LiveData<AsteroidApiStatus>
        get() = _status


    val pictureOfToday: LiveData<PictureOfDay>
        get() = _pictureOfToday

    val navigateToSelectedProperty: LiveData<Asteroid>
        get() = _navigateToSelectedProperty

    //Repository
    private val database = getDatabase(application)
    private val asteroidRepository =
        AsteroidRepository(database)


    //init is called immediately when this ViewModel is created
    init {

        viewModelScope.launch {

            _status.value = AsteroidApiStatus.LOADING

            asteroidRepository.refreshAsteroids()
            asteroidRepository.refreshPictureOfDay()

            _status.value = AsteroidApiStatus.DONE
        }
    }

    //Get data from repository
    val asteroidList = asteroidRepository.asteroids
    val pictureOfDayList = asteroidRepository.getPictureOfDay()


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
//        getAsteroidInformation(filter)
        Timber.i("Menu item choosen: ${filter}")
        when (filter) {
            AsteroidFilter.SHOW_TODAY -> asteroidRepository
            else -> AsteroidFilter.SHOW_WEEK
        }
    }

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}

