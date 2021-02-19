package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

enum class AsteroidFilter(val value: String){SHOW_WEEK("week"), SHOW_TODAY("today"), SHOW_SAVE("saved")}

// Use the Retrofit builder to build a retrofit object using a SCALER converter with our Moshi

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.BASE_URL)
    .build()

interface AsteroidApiService {

    /**
     * Returns a Coroutine [Asteroid] as string to get parse by Moshi utility
     *  which can be fetched with await() if in a Coroutine scope.
     * The @GET annotation indicates that the "realestate" endpoint will be requested with the GET
     * HTTP method
     */

    @GET(Constants.ASTEROID_FEED)
     suspend fun getAsteroids(@Query(Constants.START_DATE) startDate: String,
                             @Query(Constants.END_DATE) endDate: String,
                             @Query(Constants.API_KEY) apiKey: String) : String

//    @GET(Constants.API_KEY)
//    suspend fun getImageOfToday(@Query("filter") type: String) : ImageOfToday
}

//Expose the Retrofit Service to the rest of the application as AsteroidApid
object AsteroidApi {
    val retrofitService : AsteroidApiService by lazy { retrofit.create(AsteroidApiService::class.java) }
}








