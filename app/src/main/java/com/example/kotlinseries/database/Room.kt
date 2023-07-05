package com.example.kotlinseries.database

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Room
import androidx.room.RoomDatabase

@Dao
interface AppDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: TableUser)
}


@Database(entities = [TableUser::class], version = 1)
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