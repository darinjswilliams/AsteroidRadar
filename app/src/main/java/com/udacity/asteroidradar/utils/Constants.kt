package com.udacity.asteroidradar.utils

import com.udacity.asteroidradar.BuildConfig

object Constants {


    const val MEDIA_TYPE = "image"
    const val API_QUERY_DATE_FORMAT = "YYYY-MM-dd"
    const val DEFAULT_END_DATE_DAYS = 7
    const val BASE_URL = "https://api.nasa.gov"
    const val IMAGE_OF_TODAY_URL = "/planetary/apod"
    const val ASTEROID_FEED = "/neo/rest/v1/feed"
    const val START_DATE = "start_date"
    const val END_DATE = "end_date"
    const val API_KEY = "api_key"
    const val key =  BuildConfig.CONSUMER_KEY
}