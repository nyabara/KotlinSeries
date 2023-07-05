package com.example.kotlinseries.database

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kotlinseries.ui.House
import java.io.File

@Dao
interface AppDao {

    @Query("SELECT * FROM houses ORDER BY date DESC")
    suspend fun getAll(): List<HouseEntry>

    @Query(
        "SELECT * FROM houses WHERE " +
                "house_type LIKE :queryString OR electricity_Type LIKE :queryString " +
                "ORDER BY house_type ASC"
    )
    fun houseByName(queryString: String): PagingSource<Int, HouseEntry>

    suspend fun getAllWithFiles(photoFolder: File): List<House> {
        return getAll().map { House.fromHouseEntry(it, photoFolder) }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: UserEntry)

    //Insert Fetched houses to the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHouse( house: HouseEntry)

    //delete house
    @Delete
    suspend fun delete(house: HouseEntry)

    //illustrate a search criteria og houses in db

}


@Database(entities = [UserEntry::class,HouseEntry::class], version = 1)
abstract class KotlinSeriesDatabase : RoomDatabase() {
    abstract val appDao: AppDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: KotlinSeriesDatabase

        fun getDatabase(context: Context): KotlinSeriesDatabase {
            synchronized(KotlinSeriesDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        KotlinSeriesDatabase::class.java,
                        "kotlinseries_db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }
    }
}