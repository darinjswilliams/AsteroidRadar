package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


//Moshi to parse
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()


interface PictureApiService {

    @GET(Constants.IMAGE_OF_TODAY_URL)
    suspend fun getImageOfToday(@Query(Constants.API_KEY) apiKey: String) : PictureOfDay
}

//Expose the Retrofit Service to the rest of the application as AsteroidApid
object PictureApi {
    val pictureService : PictureApiService by lazy { retrofit.create(PictureApiService::class.java) }
}