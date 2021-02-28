package com.udacity.asteroidradar.main

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkInfo
import android.os.Build
import android.view.animation.Transformation
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.AsteroidFilter
import com.udacity.asteroidradar.api.PictureApi
import com.udacity.asteroidradar.database.PictureOfDayEntity
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.domain.asPictureDatabaseModel
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.utils.Constants
import kotlinx.coroutines.launch
import timber.log.Timber


enum class AsteroidApiStatus { LOADING, ERROR, DONE }

@RequiresApi(Build.VERSION_CODES.M)
class MainViewModel(application: Application) : AndroidViewModel(application) {

    /** External Variables **/
    private val _status = MutableLiveData<AsteroidApiStatus>()

    private val _pictureOfToday = MutableLiveData<PictureOfDay>()

    //Navigation, create property an expose it as a MutableLiveData
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

    val asteroidList = asteroidRepository.asteroids

    //init is called immediately when this ViewModel is created
    init {
        viewModelScope.launch {

            _status.value = AsteroidApiStatus.LOADING

            asteroidRepository.refreshAsteroids()
            asteroidRepository.refreshPictureOfDay()


            if (checkForActiveNetworkConnection(application)) {
                Timber.i("Connected to Internet")
                _pictureOfToday.value = PictureApi.pictureService.getImageOfToday(Constants.key)
            } else {
                //Get Picture from Cache
                _pictureOfToday.value = asteroidRepository.picOfDay.value

            }
            _status.value = AsteroidApiStatus.DONE
        }

    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedProperty.value = asteroid
    }

    //To prevent unwanted navigation
    fun displayAsteroidDetailsCompleted() {
        _navigateToSelectedProperty.let {
            it.value = null
        }
    }

    /**
     * Updates the data set filter for the web services by querying the data with the new filter
     * by returning the Domain Model for Asteroids
     * @param filter the [AsteroidApiFilter] that is sent as part of the web server request
     */
    fun updateFilter(filter: AsteroidFilter) {

        Timber.i("Menu item choosen: ${filter}")
        return asteroidRepository.getAsteroidsByDate(filter)

    }

    private fun checkForActiveNetworkConnection(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.getNetworkCapabilities(cm.activeNetwork)


        val isConnected: Boolean = activeNetwork?.hasCapability(NET_CAPABILITY_INTERNET) == true
        return isConnected
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

