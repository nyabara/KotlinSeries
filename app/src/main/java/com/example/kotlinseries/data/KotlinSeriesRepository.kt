package com.example.kotlinseries.data

import com.example.kotlinseries.database.KotlinSeriesDatabase
import com.example.kotlinseries.database.TableUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class KotlinSeriesRepository {

    //a function that registers user to db
    suspend fun addUser(user: TableUser){
        withContext(Dispatchers.IO){
            try {
                //database.appDao.insertUser(user)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}