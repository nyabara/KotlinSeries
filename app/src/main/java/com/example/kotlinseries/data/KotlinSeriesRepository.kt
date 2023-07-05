package com.example.kotlinseries.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.kotlinseries.database.HouseEntry
import com.example.kotlinseries.database.KotlinSeriesDatabase
import com.example.kotlinseries.database.UserEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.lang.Exception

class KotlinSeriesRepository(
    private val database: KotlinSeriesDatabase
) {

    //a function that registers user to db
    suspend fun addUser(user: UserEntry) {
        withContext(Dispatchers.IO) {
            try {
                //database.appDao.insertUser(user)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getSearchResultStream(query: String): Flow<PagingData<HouseEntry>> {

        Log.d("HouseSearch", "New query: $query")
        // appending '%' so we can allow other characters to be before and after the query string
        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory = { database.appDao.houseByName(dbQuery) }

        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = pagingSourceFactory

        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }
}