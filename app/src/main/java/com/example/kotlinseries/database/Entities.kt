package com.example.kotlinseries.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_t")
data class TableUser(
    @PrimaryKey
    val id:Int,
    val firstname: String,
    val middlename: String,
    val surname: String,
    val email: String,
    val phone: String,
    val password: String,
    val status: Int
)