package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


//Database Access Objects Section
@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsteroid(vararg asteroids: AsteroidEntity)

    @Query("SELECT * FROM asteroids_table WHERE close_approach_date =" +
            " :date ORDER BY close_approach_date DESC")
    fun getTodayAsteroids(date: String): LiveData<List<AsteroidEntity>>

    @Query(
        "SELECT * FROM asteroids_table WHERE close_approach_date BETWEEN :startDate " +
                "AND :endDate ORDER BY close_approach_date DESC"
    )
    fun getWeeklyAsteroids(startDate: String, endDate: String): LiveData<List<AsteroidEntity>>

    @Query("DELETE FROM asteroids_table")
    suspend fun clearAsteroid()
}

@Dao
interface PictureOfDayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPicture(pictureOfDay: PictureOfDayEntity)

    @Query("DELETE FROM picture_of_day_table")
    suspend fun clearPictureOfDay()

    @Query("SELECT * FROM picture_of_day_table WHERE target_date = :targetDate")
    fun getPictureOfTodayImage(targetDate: String) : LiveData<PictureOfDayEntity>
}

//Database Sections

@Database(
    entities = [AsteroidEntity::class, PictureOfDayEntity::class],
    version = 1,
    exportSchema = false
)


abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val pictureOfDayDao: PictureOfDayDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext, AsteroidDatabase::class.java,
                "asteroids"
            ).build()
        }
    }
    return INSTANCE
}



