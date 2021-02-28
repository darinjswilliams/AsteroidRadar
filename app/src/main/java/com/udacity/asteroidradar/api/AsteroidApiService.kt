package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

enum class AsteroidFilter(val value: String){SHOW_WEEK("week"), SHOW_TODAY("today"), SHOW_SAVE("saved")}

// Use the Retrofit builder to build a retrofit object using a SCALER converter with our Moshi
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
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

    @GET(Constants.IMAGE_OF_TODAY_URL)
    suspend fun getImageOfToday(@Query(Constants.API_KEY) apiKey: String) : PictureOfDay
}

//Expose the Retrofit Service to the rest of the application as AsteroidApid
object AsteroidApi {
    val retrofitService : AsteroidApiService by lazy { retrofit.create(AsteroidApiService::class.java) }
}








