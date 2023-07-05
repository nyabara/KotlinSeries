package com.example.kotlinseries.ui

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.example.kotlinseries.database.HouseEntry
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

data class House(
    //val house_id: Int,
    //val car_parking: Boolean,
    //val electricity_description: String,
    //val renter_id: Int,
    //val house_type_id: Int,
    //val electricity_type_id: Int,
    //val status: Int,
    //val latitude: Double,
    //val longitude: Double,
    //val other_description: String?,
    val date: String,
    val electricity_type: String,
    val has_watchman: Boolean,
    val has_water: Boolean,
    val house_type: String,
    val place:String,
    val own_compound: Boolean,
    val photos: List<File>
) {
    val timeInMillis = SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date)!!.time

    fun toHouseEntry(): HouseEntry {
        return HouseEntry(
            date = date,
            electricity_type = electricity_type,
            has_watchman = has_watchman,
            has_water = has_water,
            house_type = house_type,
            place = place,
            own_compound = own_compound,
            photo1 = photos[0].name,
            photo2 = photos.getOrNull(1)?.name,
            photo3 = photos.getOrNull(2)?.name,
            photo4 = photos.getOrNull(3)?.name,
            photo5 = photos.getOrNull(4)?.name
        )
    }

    companion object {
        fun fromHouseEntry(houseEntry: HouseEntry, photoFolder: File): House {
            return House(
                date = houseEntry.date,
                electricity_type = houseEntry.electricity_type,
                has_watchman = houseEntry.has_watchman,
                has_water = houseEntry.has_water,
                house_type = houseEntry.house_type,
                place = houseEntry.place,
                own_compound = houseEntry.own_compound,
                photos = listOfNotNull(houseEntry.photo1, houseEntry.photo2, houseEntry.photo3,houseEntry.photo4,houseEntry.photo5).map {
                    File(photoFolder, it)
                }
            )
        }
    }

}