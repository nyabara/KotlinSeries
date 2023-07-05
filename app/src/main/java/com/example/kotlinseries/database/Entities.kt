package com.example.kotlinseries.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_t")
data class UserEntry(
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

@Entity(tableName = "houses")
data class HouseEntry constructor(
//    @PrimaryKey
//    @ColumnInfo(name = "house_id")
//    val house_id: Int,
//    @ColumnInfo(name = "car_parking")
//    val car_parking: Boolean,
//    @ColumnInfo(name = "electricity_description")
//    val electricity_description: String,
//    @ColumnInfo(name = "electricity_name")
//    @ColumnInfo(name = "latitude")
//    val latitude: Double,
//    @ColumnInfo(name = "longitude")
//    val longitude: Double,
//    @ColumnInfo(name = "other_description")
//    val other_description: String?,
//    @ColumnInfo(name = "renter_id")
//    val renter_id: Int,
//    @ColumnInfo(name = "house_type_id")
//    val house_type_id: Int,
//    @ColumnInfo(name = "electricity_type_id")
//    val electricity_type_id: Int,
    //@ColumnInfo(name = "status")
    //val status: Int,
    @PrimaryKey val date: String, // date format: yyyy-MM-dd
    @ColumnInfo(name = "electricity_Type") val electricity_type: String,
    @ColumnInfo(name = "has_watchman") val has_watchman: Boolean,
    @ColumnInfo(name = "has_water") val has_water: Boolean,
    @ColumnInfo(name = "place") val place: String,
    @ColumnInfo(name = "house_type")val house_type: String,
    @ColumnInfo(name = "own_compound") val own_compound: Boolean,
    @ColumnInfo(name = "photo1_name") val photo1: String,
    @ColumnInfo(name = "photo2_name") val photo2: String? = null,
    @ColumnInfo(name = "photo3_name") val photo3: String? = null,
    @ColumnInfo(name = "photo4_name") val photo4: String? = null,
    @ColumnInfo(name = "photo5_name") val photo5: String? = null
)
const val MAX_LOG_PHOTOS_LIMIT = 5